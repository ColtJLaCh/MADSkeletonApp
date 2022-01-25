package coltonlachance.com.madskeletonapplication;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

/**CustomViewPageAdapter
 * A custom adapter for a viewpager2
 * @author Colton LaChance
 */
public class CustomViewPageAdapter extends FragmentStateAdapter {

    public CustomViewPageAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    public Fragment createFragment(int position) {
        switch(position) {
            case 0: return VPFragment.newInstance("PLACE PARAMS HERE","param2");
            //Add more cases here
            default: return VPFragment.newInstance("DEFAULT","param2");
        }
    }

    @Override
    public int getItemCount() {
        return 1;
    } //Update this number to reflect amount of pages
}