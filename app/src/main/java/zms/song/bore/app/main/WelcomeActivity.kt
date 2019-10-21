package zms.song.bore.app.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.content_welcome.*
import zms.song.bore.app.R
import zms.song.bore.app.bmob.BmobActivity
import zms.song.bore.base.CircularRevealBaseActivity
import zms.song.bore.extend.applyAppearAnim

/**
 * @author song
 */
class WelcomeActivity : CircularRevealBaseActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var mNavigationView: NavigationView
    private lateinit var mDrawer: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)
        setActionBar()
        updateTitle("HelloWorldTitle")

        mAppBar = findViewById(R.id.app_bar_layout)
//        mAppBar?.let {
//            it.setLayoutHeight(mActionLayoutHeight)
//            it.translationY = (-mActionLayoutHeight).toFloat()
//        }

        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

        mDrawer = findViewById(R.id.drawer_layout)
        mDrawer.let {
            val toggle = ActionBarDrawerToggle(
                    this@WelcomeActivity,
                    it,
                    mToolbar,
                    R.string.navigation_drawer_open,
                    R.string.navigation_drawer_close)
            it.addDrawerListener(toggle)
            toggle.syncState()
        }
        mNavigationView = findViewById(R.id.nav_view)
        mNavigationView.setNavigationItemSelectedListener(this)

        //初始化揭露动画设置
        setPreDrawView(mDrawer)
        setPreDrawAction {
            removePreDrawView(mDrawer)
            runCircularRevealAnim(mDrawer, true, circularRevealX, circularRevealY)
        }
        setCircularRevealHideEndAction {
            finish()
        }
        setCircularRevealShowEndAction {
            //applyToolbarAnim()
        }

        testImage.setOnClickListener { v ->
            startActivity(Intent(this, BmobActivity::class.java))
        }
    }

    private var mExitActiveTime: Long = 0
    override fun onBackPressed() {
        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawer(GravityCompat.START)
        } else {
            if (supportFragmentManager.backStackEntryCount <= 1) {
                if (System.currentTimeMillis() - mExitActiveTime > 2500) {
                    mExitActiveTime = System.currentTimeMillis()
                } else {
                    runCircularRevealAnim(mDrawer, false)
                }
            } else {
                super.onBackPressed()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.welcome, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> {
                // Handle the camera action
            }
            R.id.nav_gallery -> {

            }
            R.id.nav_slideshow -> {

            }
            R.id.nav_tools -> {

            }
            R.id.nav_share -> {

            }
            R.id.nav_send -> {

            }
        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun applyToolbarAnim() {
        mAppBar?.applyAppearAnim(mActionLayoutHeight.toFloat(), 0f, true)
    }
}
