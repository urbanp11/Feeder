package cz.cvut.fit.urbanp11.main.data.provider;

import android.content.UriMatcher;
import android.net.Uri;

/**
 * Created by Petr Urban on 07.04.15.
 */
public class Descriptor {

    private Descriptor() {}

    public static final String AUTHORITY = "cz.cvut.fit.urbanp11.feeder.databaseprovider";
    private static final Uri BASE_URI = Uri.parse("content://" + AUTHORITY);
    public static final UriMatcher URI_MATCHER = buildUriMatcher();


    private static  UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(AUTHORITY, ArticleDescriptor.PATH, ArticleDescriptor.PATH_TOKEN);
        matcher.addURI(AUTHORITY, ArticleDescriptor.PATH_FOR_ID, ArticleDescriptor.PATH_FOR_ID_TOKEN);

        return matcher;
    }


    public static class ArticleDescriptor {

        public static final String CONTENT_TYPE = "vnd.urbanp11.cursor.dir/vnd.cz.cvut.fit.urbanp11.articletable";
        public static final String CONTENT_TYPE_ITEM = "vnd.urbanp11.cursor.item/vnd.cz.cvut.fit.urbanp11.articletable";

        public static final String PATH = "articles";
        public static final int PATH_TOKEN = 100;
        public static final String PATH_FOR_ID = "article/#";
        public static final int PATH_FOR_ID_TOKEN = 200;

        public static final Uri CONTENT_URI = BASE_URI.buildUpon().appendPath(PATH).build();
    }
}
