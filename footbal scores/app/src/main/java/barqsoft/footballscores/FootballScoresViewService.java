package barqsoft.footballscores;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Developer: chipset
 * Package : barqsoft.footballscores
 * Project : footbal scores
 * Date : 27/12/15
 */
public class FootballScoresViewService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new FootballScoreViewFactory(this.getApplicationContext());
    }

    class FootballScoreViewFactory implements RemoteViewsFactory {
        public static final int COL_HOME = 3;
        public static final int COL_AWAY = 4;
        public static final int COL_HOME_GOALS = 6;
        public static final int COL_AWAY_GOALS = 7;
        public static final int COL_DATE = 1;
        public static final int COL_LEAGUE = 5;
        public static final int COL_MATCHDAY = 9;
        public static final int COL_ID = 8;


        private Cursor mCursor;
        private Context mContext;
        private String fragmentdate;
        private Utilies utilies;

        public FootballScoreViewFactory(Context context) {
            this.mContext = context;
            this.utilies = new Utilies(context);
        }

        @Override
        public void onCreate() {
            setup();
        }

        @Override
        public void onDataSetChanged() {
            setup();
        }

        @Override
        public void onDestroy() {

        }

        @Override
        public int getCount() {
            return mCursor.getCount();
        }

        @Override
        public RemoteViews getViewAt(int position) {
            RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.scores_widget_list_item);
            if (mCursor != null && !mCursor.isAfterLast()) {
                views.setTextViewText(R.id.home_name, mCursor.getString(COL_HOME));
                views.setTextViewText(R.id.away_name, mCursor.getString(COL_AWAY));
                views.setTextViewText(R.id.data_textview, utilies.getLeague(mCursor.getInt(COL_LEAGUE)));
                views.setTextViewText(R.id.score_textview, utilies.getScores(mCursor.getInt(COL_HOME_GOALS), mCursor.getInt(COL_AWAY_GOALS)));

                Bundle extras = new Bundle();
                extras.putInt(FootballScoreAppWidgetProvider.EXTRA_ITEM, position);
                Intent fillInIntent = new Intent();
                fillInIntent.putExtras(extras);
                views.setOnClickFillInIntent(R.id.widget_list_item, fillInIntent);

                mCursor.moveToNext();
            }
            return views;
        }

        @Override
        public RemoteViews getLoadingView() {
            return new RemoteViews(mContext.getPackageName(), R.layout.widget_loading_layout);
        }

        @Override
        public int getViewTypeCount() {
            return 2;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }


        private void setup() {
            fragmentdate = new SimpleDateFormat("yyyy-MM-dd").format(new Date(System.currentTimeMillis()));
            mCursor = mContext.getContentResolver().query(DatabaseContract.scores_table.buildScoreWithDate(),
                    null, null, new String[]{fragmentdate}, null);
            if (mCursor != null && mCursor.getCount() > 0)
                mCursor.moveToFirst();
        }
    }
}
