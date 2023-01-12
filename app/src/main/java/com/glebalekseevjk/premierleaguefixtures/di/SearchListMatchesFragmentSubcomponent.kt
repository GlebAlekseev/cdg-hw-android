package com.glebalekseevjk.premierleaguefixtures.di

import com.glebalekseevjk.premierleaguefixtures.ui.fragment.ListMatchesFragment
import com.glebalekseevjk.premierleaguefixtures.ui.fragment.SearchListMatchesFragment
import dagger.Subcomponent

@Subcomponent
interface SearchListMatchesFragmentSubcomponent {
    fun inject(searchListMatchesFragment: SearchListMatchesFragment)
}