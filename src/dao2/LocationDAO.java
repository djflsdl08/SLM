package dao2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import model.Location;
import account.Database;

public class LocationDAO {
	
	private static LocationDAO locationDAO = null;
	private Connection conn = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;
	private Database dbAccount;
	
	public LocationDAO() {
		dbAccount = new Database();
	}
	
	public static LocationDAO getInstance() {
		if(locationDAO == null){
			locationDAO = new LocationDAO();
		}
		return locationDAO;
	}
	
	public void dbConnect() {
        String url = dbAccount.getUrl();
        String jdbc = dbAccount.getJdbc();
        String user = dbAccount.getUser();
        String pass = dbAccount.getPass();
        
        try {
            Class.forName(jdbc);
            
            conn = DriverManager.getConnection(url, user, pass);
        } catch(Exception e) {
            System.out.println("SQLException: " + e.getMessage());
        } finally{
        }
	}
	
	public void close(Connection conn, PreparedStatement pstmt){
        try{
            if ( pstmt != null){ pstmt.close(); }
        }catch(Exception e){}
        
        try{
            if ( conn != null){ conn.close(); }
        }catch(Exception e){}        
    }
    
    public void close(Connection conn, PreparedStatement pstmt, ResultSet rs){
        try{
            if ( rs != null){ rs.close(); }
        }catch(Exception e){}
        
        try{
            if ( pstmt != null){ pstmt.close(); }
        }catch(Exception e){}
        
        try{
            if ( conn != null){ conn.close(); }
        }catch(Exception e){}        
    }
	
	public void insertLocation(Location location) {
		try {
			dbConnect();
			String sql = "insert into locationdata(client_id, latitude, longitude, time) values(?,?,?,?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, location.getClient_id());
			pstmt.setDouble(2, location.getLatitude());
			pstmt.setDouble(3, location.getLongitude());
			pstmt.setInt(4, location.getTime());
			
			pstmt.executeUpdate();
		} catch(Exception e) {
			System.out.println("SQLException: " + e.getMessage());
		} finally {
			close(conn, pstmt);
		}
	}
	
	public Location getLeastLocation(){
		Location location =  new Location();
		try {
			dbConnect();
			String sql = "select latitude, longitude from testmovelocation where idtestmovelocation = (select max(idtestmovelocation) from testmovelocation);";
			
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				location.setLatitude(rs.getDouble("latitude"));
				location.setLongitude(rs.getDouble("longitude"));
			}
		} catch(Exception e) {
			System.out.println("SQLException: " + e.getMessage());
		} finally {
			close(conn, pstmt, rs);
		}
		return location;
	}
	
	public List<Location> getAllLeastLocation(){
		List<Location> listLocation = new ArrayList<Location>();
		try {
			dbConnect();
			String sql = "select * from locationdata where (client_id, time) in "
					+ "(select client_id, max(time) from locationdata group by client_id);";
			
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				Location location = new Location();
				location.setId(rs.getInt("id"));
				location.setClient_id(rs.getString("client_id"));
				location.setLatitude(rs.getDouble("latitude"));
				location.setLongitude(rs.getDouble("longitude"));
				location.setTime(rs.getInt("time"));
				listLocation.add(location);
			}
		} catch(Exception e) {
			System.out.println("SQLException: " + e.getMessage());
		} finally {
			close(conn, pstmt, rs);
		}
		System.out.println(listLocation);
		return listLocation;
	}
	
	public List<String> getClientsAboutLocation(Double[] locations) {
		List<String> clients = new ArrayList<String>();
		try {
			dbConnect();
			String sql = "select client_id from locationdata where (client_id,time) in "
					+ "(select client_id,max(time) from locationdata "
					+ "where latitude<=? and latitude>=? and longitude<=? "
					+ "and longitude>=? group by client_id);";
			
			// 37.55022105257947,127.13762815237351
			// 37.5102109744057,127.08662815237346
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setDouble(1, locations[0]);
			pstmt.setDouble(2, locations[2]);
			pstmt.setDouble(3, locations[1]);
			pstmt.setDouble(4, locations[3]);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				clients.add(rs.getString("client_id"));
			}
		} catch(Exception e) {
			System.out.println("SQLException: " + e.getMessage());
		} finally {
			close(conn, pstmt, rs);
		}
		return clients;
	}
	
	public int getUsersCountByDistrict(String district, String date) {
		int count = 0;
		
		//select * from locationdata where time > DATE_SUB("20180517102530", INTERVAL 30 SECOND);
		
		try {
			dbConnect();
			String sql = "select client_id from locationdata where district like ? "
					+ "and time >= DATE_SUB(?, INTERVAL 30 SECOND) and time <= STR_TO_DATE(?,\"%Y%m%d%k%i%s\")";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, district + '%');
			pstmt.setString(2, date);
			pstmt.setString(3, date);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				count++;
			}
			
		} catch(Exception e) {
			System.out.println("SQLException: " + e.getMessage());
		} finally {
			close(conn, pstmt, rs);
		}
		
		System.out.println(count);
		return count;
	}
}
