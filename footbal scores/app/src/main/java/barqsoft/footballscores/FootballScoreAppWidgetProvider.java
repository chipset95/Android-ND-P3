package barqsoft.footballscores;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

/**
 * Developer: chipset
 * Package : barqsoft.footballscores
 * Project : footbal scores
 * Date : 27/12/15
 */
public class FootballScoreAppWidgetProvider extends AppWidgetProvider {

    public static final String EXTRA_ITEM = "item_pos";
    public static final String ACTION_ACT = "activity";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {

            Intent intent = new Intent(context, FootballScoresViewService.class);

            Intent toActivity = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, toActivity, 0);

            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
            views.setRemoteAdapter(appWidgetId, R.id.scores_list, intent);
            views.setOnClickPendingIntent(R.id.widget_item, pendingIntent);

            Intent activityIntent = new Intent(context, FootballScoreAppWidgetProvider.class);
            activityIntent.setAction(ACTION_ACT);
            PendingIntent activityPendingIntent = PendingIntent.getBroadcast(context, 0, activityIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            views.setPendingIntentTemplate(R.id.scores_list, activityPendingIntent);

            appWidgetManager.updateAppWidget(appWidgetId, views);

        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(ACTION_ACT)) {
            context.startActivity(new Intent(context, MainActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    .putExtra(EXTRA_ITEM, intent.getIntExtra(EXTRA_ITEM, 0)));
        }
        super.onReceive(context, intent);
    }
}
