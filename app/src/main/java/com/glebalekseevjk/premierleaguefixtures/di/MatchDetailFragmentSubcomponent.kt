package com.glebalekseevjk.premierleaguefixtures.di

import com.glebalekseevjk.premierleaguefixtures.ui.fragment.MatchDetailFragment
import dagger.Subcomponent

@Subcomponent
interface MatchDetailFragmentSubcomponent {
    fun inject(matchDetailFragment: MatchDetailFragment)
}