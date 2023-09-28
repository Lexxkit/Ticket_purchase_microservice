package com.lexxkit.stmmicroservices.ticketpurchase.constant;

public class TicketSqlQueries {
  public static final String SELECT_JOIN_TABLES_BASE_QUERY = "select "
      + "t.*, r.id as r_id, r.start_point, r.end_point, c.id as c_id, c.title from tickets t "
      + "left join routes r on t.route_id = r.id "
      + "left join carriers c on r.carrier_id = c.id";
  public static final String AVAILABLE_WHERE_FILTER = "where t.is_available = true AND t.date_time > NOW()";
  public static final String DATE_WHERE_FILTER = "AND t.date_time = :dateTime";
  public static final String POINT_WHERE_FILTER = "AND (LOWER(r.start_point) LIKE LOWER(:startEndPoint)"
      + " OR LOWER(r.end_point) LIKE LOWER(:startEndPoint))";
  public static final String CARRIER_TITLE_WHERE_FILTER = "AND LOWER(c.title) LIKE LOWER(:title)";
  public static final String USER_WHERE_FILTER = "where user_id = :user_id order by t.date_time ASC";
  public static final String ORDER_LIMIT_OFFSET = "order by t.id limit :limit offset :offset";
  public static final String SELECT_TICKET_BY_ID = "select id, date_time, seat_number, price, "
      + "route_id, is_available from tickets where id = :id";
  public static final String UPDATE_TICKET_QUERY = "update tickets set "
      + "is_available = :is_available, user_id = :user_id where id = :id";
}
