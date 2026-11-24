package org.basicos.trebuchet;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/** A small, standalone HOME app that only uses public Android APIs (Android 11+). */
public final class LauncherActivity extends Activity {
    private final int paddingDp = 16;

    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        showApps();
    }

    @Override public void onResume() {
        super.onResume();
        if (getWindow().getDecorView() != null) showApps();
    }

    private void showApps() {
        LinearLayout content = new LinearLayout(this);
        content.setOrientation(LinearLayout.VERTICAL);
        content.setPadding(dp(paddingDp), dp(18), dp(paddingDp), dp(paddingDp));
        content.setBackgroundColor(Color.rgb(16, 19, 24));

        TextView title = new TextView(this);
        title.setText("Trebuchet");
        title.setTextColor(Color.WHITE);
        title.setTextSize(30);
        title.setGravity(Gravity.CENTER_VERTICAL);
        content.addView(title, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dp(54)));

        TextView subtitle = new TextView(this);
        subtitle.setText("All apps");
        subtitle.setTextColor(Color.rgb(190, 198, 215));
        subtitle.setTextSize(15);
        content.addView(subtitle, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dp(34)));

        ScrollView scroller = new ScrollView(this);
        GridLayout grid = new GridLayout(this);
        int columns = getResources().getConfiguration().screenWidthDp >= 600 ? 6 : 4;
        grid.setColumnCount(columns);
        grid.setUseDefaultMargins(false);
        for (ResolveInfo app : installedApps()) grid.addView(appTile(app, columns));
        scroller.addView(grid);
        content.addView(scroller, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1));
        setContentView(content);
    }

    private List<ResolveInfo> installedApps() {
        Intent query = new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> apps = new ArrayList<>(getPackageManager().queryIntentActivities(query, PackageManager.MATCH_ALL));
        apps.removeIf(info -> getPackageName().equals(info.activityInfo.packageName));
        Collections.sort(apps, Comparator.comparing(info -> info.loadLabel(getPackageManager()).toString(), String.CASE_INSENSITIVE_ORDER));
        return apps;
    }

    private View appTile(ResolveInfo info, int columns) {
        LinearLayout tile = new LinearLayout(this);
        tile.setOrientation(LinearLayout.VERTICAL);
        tile.setGravity(Gravity.CENTER_HORIZONTAL);
        tile.setPadding(dp(5), dp(8), dp(5), dp(8));
        tile.setClickable(true);
        tile.setForeground(selectableBackground());

        TextView icon = new TextView(this);
        icon.setGravity(Gravity.CENTER);
        icon.setBackground(info.loadIcon(getPackageManager()));
        tile.addView(icon, new LinearLayout.LayoutParams(dp(52), dp(52)));

        TextView name = new TextView(this);
        name.setText(info.loadLabel(getPackageManager()));
        name.setTextColor(Color.WHITE);
        name.setTextSize(12);
        name.setGravity(Gravity.CENTER);
        name.setMaxLines(2);
        tile.addView(name, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dp(38)));
        tile.setOnClickListener(v -> launch(info));

        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
        params.width = 0;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1, 1f);
        tile.setLayoutParams(params);
        return tile;
    }

    private void launch(ResolveInfo info) {
        Intent intent = new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LAUNCHER);
        intent.setClassName(info.activityInfo.packageName, info.activityInfo.name);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        try { startActivity(intent); } catch (SecurityException ignored) { }
    }

    private android.graphics.drawable.Drawable selectableBackground() {
        GradientDrawable background = new GradientDrawable();
        background.setColor(Color.TRANSPARENT);
        return background;
    }

    private int dp(int value) { return Math.round(value * getResources().getDisplayMetrics().density); }
}
