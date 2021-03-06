package net.sourceforge.squirrel_sql.fw.sql;

public class QueryHolder
{
   private final String _query;
   private final String _originalQuery;

   public QueryHolder(String query)
   {
      this(query, query);
   }

   public QueryHolder(String query, String originalQuery)
   {
      _query = query;
      _originalQuery = originalQuery;
   }

   public String getQuery()
   {
      return _query;
   }

   public String getOriginalQuery()
   {
      return _originalQuery;
   }
}
