package com.example.premierleaguefixtures

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null){
            launchFragment(BlackFragment())
        }
    }

    private fun launchFragment(fragment: Fragment) {
        // supportFragmentManager ссылка на FragmentManager привязанный к данной активити
        // beginTransaction - используя апи FragmentTransaction фиксируется набор действий по отношению к фрагментам
        // add - помещает фрагмент в контейнер
        // commit - добавление фиксации в очередь на исполнение в главном потоке

        supportFragmentManager.beginTransaction()
            .add(R.id.main_fcv, fragment)
            .commit()
    }
}