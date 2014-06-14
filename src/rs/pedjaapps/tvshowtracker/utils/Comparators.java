package rs.pedjaapps.tvshowtracker.utils;

import java.util.Comparator;

import rs.pedjaapps.tvshowtracker.model.Episode;
import rs.pedjaapps.tvshowtracker.model.EpisodeDao;
import rs.pedjaapps.tvshowtracker.model.Season;
import rs.pedjaapps.tvshowtracker.model.Show;

/**
 * Created by pedja on 7.6.14..
 */
public class Comparators
{

    public static class NameComparator implements Comparator<Show>
    {
        boolean ascending;

        public NameComparator(boolean ascending)
        {
            this.ascending = ascending;
        }

        @Override
        public int compare(Show s1, Show s2)
        {
            String sub1 = s1.getTitle();
            String sub2 = s2.getTitle();
            return ascending ? sub1.compareTo(sub2) : sub2.compareTo(sub1);
        }
    }

    public static class NextEpisodeComparator implements Comparator<Show>
    {
        boolean ascending;

        public NextEpisodeComparator(boolean ascending)
        {
            this.ascending = ascending;
        }

        @Override
        public int compare(Show p1, Show p2)
        {
            if (p1.getNextEpisodeHours() > p2.getNextEpisodeHours())
                return ascending ?-1 : 1;
            if (p1.getNextEpisodeHours() < p2.getNextEpisodeHours())
                return ascending ? 1 : -1;

            return 0;
        }
    }

    public static class NextEpisodeTimeComparator implements Comparator<Episode>
    {
        @Override
        public int compare(Episode p1, Episode p2)
        {
            if (p1.getFirst_aired() > p2.getFirst_aired())
                return -1 ;
            if (p1.getFirst_aired() < p2.getFirst_aired())
                return 1;

            return 0;
        }
    }

    public static class EpisodeHourComparator implements Comparator<Episode>
    {
        @Override
        public int compare(Episode p1, Episode p2)
        {
            if (p1.getAirsIn() > p2.getAirsIn())
                return 1 ;
            if (p1.getAirsIn() < p2.getAirsIn())
                return -1;

            return 0;
        }
    }

    public static class WatchComparator implements Comparator<Show>
    {
        boolean ascending;

        public WatchComparator(boolean ascending)
        {
            this.ascending = ascending;
        }

        @Override
        public int compare(Show p1, Show p2)
        {
            if (p1.getWatchedPercent() < p2.getWatchedPercent())
                return ascending ? -1 : 1;
            if (p1.getWatchedPercent() > p2.getWatchedPercent())
                return ascending ? 1 : -1;
            return 0;
        }
    }

    public static class SeasonNumberComparator implements Comparator<Season>
    {
        boolean ascending;

        public SeasonNumberComparator(boolean ascending)
        {
            this.ascending = ascending;
        }

        @Override
        public int compare(Season p1, Season p2)
        {
            if (p1.getSeason() < p2.getSeason())
                return ascending ? -1 : 1;
            if (p1.getSeason() > p2.getSeason())
                return ascending ? 1 : -1;
            return 0;
        }
    }

    public static class EpisodeNumberComparator implements Comparator<Episode>
    {
        boolean ascending;

        public EpisodeNumberComparator(boolean ascending)
        {
            this.ascending = ascending;
        }

        @Override
        public int compare(Episode p1, Episode p2)
        {
            if (p1.getEpisode() < p2.getEpisode())
                return ascending ? -1 : 1;
            if (p1.getEpisode() > p2.getEpisode())
                return ascending ? 1 : -1;
            return 0;
        }
    }

    public static class NetworkComparator implements Comparator<Show>
    {
        boolean ascending;

        public NetworkComparator(boolean ascending)
        {
            this.ascending = ascending;
        }

        @Override
        public int compare(Show s1, Show s2)
        {
            String sub1 = s1.getNetwork();
            String sub2 = s2.getNetwork();
            return ascending ? sub1.compareTo(sub2) : sub2.compareTo(sub1);
        }
    }

    public static class StatusComparator implements Comparator<Show>
    {
        boolean ascending;

        public StatusComparator(boolean ascending)
        {
            this.ascending = ascending;
        }

        @Override
        public int compare(Show s1, Show s2)
        {
            String sub1 = s1.getStatus();
            String sub2 = s2.getStatus();
            return ascending ? sub1.compareTo(sub2) : sub2.compareTo(sub1);
        }
    }

    public static class RatingComparator implements Comparator<Show>
    {
        boolean ascending;

        public RatingComparator(boolean ascending)
        {
            this.ascending = ascending;
        }

        @Override
        public int compare(Show p1, Show p2)
        {
            if (p1.getRating() < p2.getRating())
                return ascending ? -1 : 1;
            if (p1.getRating() > p2.getRating())
                return ascending ? 1 : -1;
            return 0;
        }
    }

    public static class RuntimeComparator implements Comparator<Show>
    {
        boolean ascending;

        public RuntimeComparator(boolean ascending)
        {
            this.ascending = ascending;
        }

        @Override
        public int compare(Show p1, Show p2)
        {
            if (p1.getRuntime() < p2.getRuntime())
                return ascending ? -1 : 1;
            if (p1.getRuntime() > p2.getRuntime())
                return ascending ? 1 : -1;
            return 0;
        }
    }
}
