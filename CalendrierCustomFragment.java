package com.superjb.mycolloc;

import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidGridAdapter;

/**
 * Created by Jean-Bryce on 23/06/2016.
 */
public class CalendrierCustomFragment extends CaldroidFragment {

    @Override
    public CaldroidGridAdapter getNewDatesGridAdapter(int month, int year) {
        // TODO Auto-generated method stub
        return new CalendrierCustomAdapter(getActivity(), month, year,
                getCaldroidData(), extraData);
    }

}
