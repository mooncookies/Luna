package com.mm.luna.ui.main;

import android.content.Intent;
import android.graphics.Color;
import androidx.annotation.NonNull;
import android.support.design.internal.NavigationMenuView;
import com.google.android.material.navigation.NavigationView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.jaeger.library.StatusBarUtil;
import com.mm.luna.R;
import com.mm.luna.base.BaseActivity;
import com.mm.luna.bean.WallPaperBean;
import com.mm.luna.ui.computer.ComputerFragment;
import com.mm.luna.ui.douban.DoubanFragment;
import com.mm.luna.ui.douban.MovieSearchActivity;
import com.mm.luna.ui.gank.GankMainFragment;
import com.mm.luna.ui.nba.LiveActivity;
import com.mm.luna.ui.nba.LiveFragment;
import com.mm.luna.ui.orange.OrangeFragment;
import com.mm.luna.ui.setting.SettingFragment;
import com.mm.luna.ui.today.TodayFragment;
import com.mm.luna.ui.violet.VioletActivity;
import com.mm.luna.ui.wan.ArticleFragment;
import com.mm.luna.ui.zhihu.ZhiHuFragment;

import java.util.Objects;

import butterknife.BindView;
import me.jessyan.retrofiturlmanager.RetrofitUrlManager;

public class MainActivity extends BaseActivity<MainContract.Presenter> implements MainContract.View {

    @BindView(R.id.drawer)
    DrawerLayout drawer;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.navigation_view)
    NavigationView navigationView;

    private int which = 0;
    private Fragment mContent;
    private Fragment targetFragment;

    private TodayFragment todayFragment;
    private ZhiHuFragment zhiHuFragment;
    private DoubanFragment doubanFragment;
    private GankMainFragment gankFragment;
    private ArticleFragment androidFragment;
    private OrangeFragment orangeFragment;
    private ComputerFragment computerFragment;
    private SettingFragment settingFragment;
    //  private ScheduleFragment scheduleFragment;
    private LiveFragment scheduleFragment;

    private MenuItem itemSearch;
    private MenuItem itemLive;

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public MainContract.Presenter initPresenter() {
        return new MainPresenter(this);
    }

    @Override
    public void initView() {
        initRetrofitDomain();
        presenter.getMonthPicture();
        StatusBarUtil.setColorForDrawerLayout(this, drawer, Color.TRANSPARENT, 0);
        if (todayFragment == null) todayFragment = new TodayFragment();
        switchContentFragment(todayFragment);
        targetFragment = todayFragment;
        toolbar.setTitle(R.string.today);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationIcon(R.mipmap.ic_drawer_home);

        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.open, R.string.close);
        drawerToggle.syncState();
        drawer.addDrawerListener(drawerToggle);
        NavigationMenuView navigationMenuItemView = (NavigationMenuView) navigationView.getChildAt(0);
        if (navigationMenuItemView != null) {
            navigationMenuItemView.setVerticalScrollBarEnabled(false);
        }
        navigationView.setCheckedItem(R.id.item_today);
        //    navigationView.getHeaderView(0).setOnClickListener(v -> violet());
        navigationView.setNavigationItemSelectedListener((MenuItem item) -> {
            itemSearch.setVisible(false);
            itemLive.setVisible(false);
            // drawer.closeDrawers();
            switch (item.getItemId()) {
                case R.id.item_today:
                    toolbar.setTitle(R.string.today);
                    if (todayFragment == null) {
                        targetFragment = todayFragment = new TodayFragment();
                    } else {
                        switchContentFragment(todayFragment);
                    }
                    break;
                case R.id.item_zhihu:
                    toolbar.setTitle(R.string.zhihu_daily);
                    if (zhiHuFragment == null) {
                        targetFragment = zhiHuFragment = new ZhiHuFragment();
                    } else {
                        switchContentFragment(zhiHuFragment);
                    }
                    break;
                case R.id.item_douban:
                    toolbar.setTitle(R.string.douban_movie);
                    itemSearch.setVisible(true);
                    if (doubanFragment == null) doubanFragment = new DoubanFragment();
                    switchContentFragment(doubanFragment);
                    break;
                case R.id.item_orange:
                    toolbar.setTitle(R.string.orange_title);
                    if (orangeFragment == null) {
                        targetFragment = orangeFragment = new OrangeFragment();
                    } else {
                        switchContentFragment(orangeFragment);
                    }
                    break;
                case R.id.item_basketball:
                    toolbar.setTitle(R.string.nba_schedule);
                    // itemLive.setVisible(true);
                    if (scheduleFragment == null) {
                        targetFragment = scheduleFragment = new LiveFragment();
                    } else {
                        switchContentFragment(scheduleFragment);
                    }
                    break;
                case R.id.item_computer:
                    toolbar.setTitle(R.string.computer_fan);
                    if (computerFragment == null) {
                        targetFragment = computerFragment = new ComputerFragment();
                    } else {
                        switchContentFragment(computerFragment);
                    }
                    break;
                case R.id.item_wan_android:
                    toolbar.setTitle(R.string.wan_android);
                    if (androidFragment == null) {
                        targetFragment = androidFragment = new ArticleFragment();
                    } else {
                        switchContentFragment(androidFragment);
                    }
                    break;
                case R.id.item_gank:
                    toolbar.setTitle(R.string.gank_io);
                    if (gankFragment == null) {
                        targetFragment = gankFragment = new GankMainFragment();
                    } else {
                        switchContentFragment(gankFragment);
                    }
                    break;
                case R.id.item_setting:
                    toolbar.setTitle(R.string.setting);
                    if (settingFragment == null) {
                        targetFragment = settingFragment = new SettingFragment();
                    } else {
                        switchContentFragment(settingFragment);
                    }
                    break;
            }
            drawer.closeDrawers();
            return true;
        });
        drawer.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerClosed(@NonNull View drawerView) {
                if (!targetFragment.isAdded()) {
                    switchContentFragment(targetFragment);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        itemSearch = menu.findItem(R.id.action_search);
        itemLive = menu.findItem(R.id.action_live);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                startActivity(new Intent(this, MovieSearchActivity.class));
                break;
            case R.id.action_live:
                startActivity(new Intent(this, LiveActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void violet() {
        String[] comicArr = {"Violet Evergarden", "Tokyo Ghoul Ⅲ"};
        new AlertDialog.Builder(MainActivity.this)
                .setTitle(R.string.please_select)
                .setSingleChoiceItems(comicArr, which, (dialogInterface, i) -> which = i)
                .setPositiveButton(R.string.sure, (dialogInterface, i) -> {
                    startActivity(new Intent(MainActivity.this, VioletActivity.class).putExtra("tag", which));
                    dialogInterface.dismiss();
                })
                .setNegativeButton(R.string.cancel, (dialogInterface, i) -> dialogInterface.dismiss())
                .create().show();
    }

    /**
     * Switch fragment.
     *
     * @param to
     */
    private void switchContentFragment(Fragment to) {
        if (mContent == null) {
            mContent = new Fragment();
        }
        if (mContent != to) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            if (!to.isAdded()) {
                transaction.hide(mContent).add(R.id.fl_content, to).commit(); // 隐藏当前的fragment，add下一个到Activity中
            } else {
                transaction.hide(mContent).show(to).commit(); // 隐藏当前的fragment，显示下一个
            }
            mContent = to;
        }
    }

    @Override
    public void onFinish() {

    }

    @Override
    public void onError() {

    }

    private void initRetrofitDomain() {
        RetrofitUrlManager.getInstance().putDomain("zhihu", "https://news-at.zhihu.com/");
        RetrofitUrlManager.getInstance().putDomain("douban", "https://api.douban.com/");
        RetrofitUrlManager.getInstance().putDomain("gank", "http://gank.io/api/");
        RetrofitUrlManager.getInstance().putDomain("wan", "http://www.wanandroid.com/");
        RetrofitUrlManager.getInstance().putDomain("orange", "https://www.juzimi.com/");
        RetrofitUrlManager.getInstance().putDomain("cfan", "http://www.cfan.com.cn/");
        RetrofitUrlManager.getInstance().putDomain("mob", "http://apicloud.mob.com/");
    }

    @Override
    public void setMonthPicture(WallPaperBean bean) {
        // Glide.with(this).load(bean.getMonth()).crossFade().into((ImageView) navigationView.getHeaderView(0).findViewById(R.id.iv_month));
//        Glide.with(this).load(bean.getData().get(0).getUrl()).crossFade().into((ImageView) navigationView.getHeaderView(0).findViewById(R.id.iv_month));
        Glide.with(this).load("http://img.netbian.com/file/2019/0509/cfa94b884a089dfa602a97e2e598b029.jpg").crossFade().into((ImageView) navigationView.getHeaderView(0).findViewById(R.id.iv_month));
    }

//    @Override
//    public void showTodayEnglish(HomeBean bean) {
////        AlertDialog dialog = new AlertDialog.Builder(this).create();
////        View view = LayoutInflater.from(this).inflate(R.layout.dialog_today_english, null);
////        dialog.setCancelable(false);
////        dialog.setContentView(R.layout.dialog_today_english );
////        dialog.show();
//        new TodayEnglishView(this, bean).showDialog();
//    }

}