Architecture - Component Diagram

This document provides a high-level component diagram for the scheduling
application and maps components to source files in the repository.

```mermaid
flowchart TB
  subgraph SchedulingService["Scheduling Service (this repo)"]
    Scheduler["Scheduler\n(core logic)"]
    Appointment["Appointment\n(model)"]
    Clinic["Clinic\n(business hours & tz)"]
    TZ["TimezoneConverter\n(UTC <-> local)"]
    Clock["Clock\n(now abstraction)"]
    Repo["AppointmentRepository\n(interface)"]
    InMemoryRepo["InMemoryAppointmentRepository\n(test impl)"]
  end

  UI["Client / UI / Consumer"] -->|requests availability / booking| Scheduler
  Scheduler -->|persist| Repo
  Repo -->|in-memory impl| InMemoryRepo
  Scheduler -->|creates/reads| Appointment
  Scheduler -->|reads rules| Clinic
  Scheduler -->|converts times| TZ
  Scheduler -->|queries time| Clock
  Scheduler -->|returns local times| TZ

  Postgres[(Postgres DB\n(optional future repo))]
  Repo -->|DB-backed repo| Postgres

  subgraph CI["CI & Tests"]
    RSpec["RSpec specs"]
    GH["GitHub Actions (CI)"]
  end
  GH --> RSpec
  RSpec --> Scheduler
  RSpec --> InMemoryRepo

  classDef component fill:#f3f4f6,stroke:#333,stroke-width:1px;
  class Scheduler,Repo,Clinic,Appointment,TZ,Clock component;
```

Component to file mapping

- Scheduler - `app/scheduler.rb` (core scheduling logic; DI for repo/clinic/clock)
- Appointment - `app/appointment.rb` (model for appointments; UTC storage)
- Clinic - `app/clinic.rb` (encapsulates clinic hours and timezone offset)
- TimezoneConverter - `app/timezone_converter.rb` (fixed-offset conversions)
- Clock - `app/clock.rb` (abstraction for current time; `FixedClock` for tests)
- AppointmentRepository - `app/appointment_repository.rb` (interface)
- InMemoryAppointmentRepository - `app/in_memory_appointment_repository.rb` (test implementation)
- Tests - `spec/*.rb` (RSpec test suite)
- CI - `.github/workflows/ruby.yml`

Notes and suggested next steps

- Persistence: swap the in-memory repository for a DB-backed implementation (Postgres). Use a `tstzrange(start, end)` with an exclusion constraint to prevent overlapping appointments at the DB layer.
- Timezones: the project currently uses fixed-offset conversions; for DST correctness, accept IANA timezone identifiers and use `tzinfo` or ActiveSupport timezone helpers.
- Concurrency: for production, perform booking inside a DB transaction and retry on constraint violation (or use advisory locks per clinic/day to serialize writes).

Place this file in `docs/architecture.md` so reviewers can quickly open it and see the component map.
