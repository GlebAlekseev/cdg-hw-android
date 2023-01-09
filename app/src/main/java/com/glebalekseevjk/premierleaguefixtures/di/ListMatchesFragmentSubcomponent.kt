package com.glebalekseevjk.premierleaguefixtures.di

import com.glebalekseevjk.premierleaguefixtures.ui.fragment.ListMatchesFragment
import dagger.Subcomponent

@Subcomponent
interface ListMatchesFragmentSubcomponent {
    fun inject(listMatchesFragment: ListMatchesFragment)
}