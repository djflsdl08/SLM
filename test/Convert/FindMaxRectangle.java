package Convert;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class District {
    Double maxLat;
    Double minLat;
    Double maxLong;
    Double minLong;
    
    public void setMaxLat(Double lat) {
        this.maxLat = lat;
    }
    
    public void setMaxLong(Double lng) {
        this.maxLong = lng;
    }
    public void setMinLat(Double lat) {
        this.minLat = lat;
    }
    public void setMinLong(Double lng) {
        this.minLong = lng;
    }
    public String toString() {
        return this.maxLong + ", "+this.maxLat +", "+ this.minLong +", "+ this.minLat;
    }
    
    public void findSurfaceArea() {
        SurfaceAreaOfDistrict.getInstance().rectangleDistrict();
    }
    
    public List<District> getSurfaceArea() {
        return SurfaceAreaOfDistrict.getInstance().surfaceAreaOfDistrict;
    }
}

class SurfaceAreaOfDistrict {
    
    public static SurfaceAreaOfDistrict surfaceAreaInstance = null;
    List<District> surfaceAreaOfDistrict = new ArrayList<District>();
    String wholeDistrict = "";
    
    Double maxLat = 0.0;	// 0-90
    Double maxLong = 0.0; 	// 0-180
    Double minLat = 0.0;
    Double minLong = 0.0;
    Double dValue;
    int i;
    
    public static SurfaceAreaOfDistrict getInstance() {
        if(surfaceAreaInstance == null) {
            surfaceAreaInstance = new SurfaceAreaOfDistrict();
        }
        return surfaceAreaInstance;
    }
    
    public void rectangleDistrict() {
        String[] str = wholeDistrict.split("District");
        String[] val, setValue;
        
        for(i=1;i<26;i++) {
            val = str[i].substring(1, str[i].length()-3).split(",");
            if(i==25) val = str[i].substring(1, str[i].length()-1).split(",");
            for(int j=0;j<val.length;j++) {
                setValue = val[j].split("=");
                dValue = Double.parseDouble(setValue[1]);
                
                switch(setValue[0]) {
                    case "maxLat" : maxLat = dValue; break;
                    case " minLat" : minLat = dValue;break;
                    case " maxLong" : maxLong = dValue; break;
                    case " minLong" : minLong = dValue; break;
                }
            }
            
            District district = new District();
            district.setMaxLat(maxLat);
            district.setMaxLong(maxLong);
            district.setMinLat(minLat);
            district.setMinLong(minLong);
            
            surfaceAreaOfDistrict.add(district);
            
        }
        //System.out.println(surfaceAreaOfDistrict);
        
    }
}

public class FindMaxRectangle {
    
    public static void findDistrict() {
		
		//String s = System.getProperty("user.dir");
		//System.out.println("현재 디렉토리는 " + s + " 입니다");
        List<District> surfaceArea = new ArrayList<District>();
		String csvFile = "Seoul_District_Table.csv";
        BufferedReader br = null;
        String line = "";
        int count = 0;
       
        try {
            br = new BufferedReader(new FileReader(csvFile));
            while ((line = br.readLine()) != null) {
            		if(count!=0) {
            			String[] str = line.split("<coordinates>");
            			String[] onlyGeo = str[1].split("</coordinates>");
            			//System.out.println(onlyGeo[0]);
            			String[] longLat = onlyGeo[0].split(",");
            			
                    System.out.println(longLat.length + ":" + longLat[0] + " " + longLat[1]+ " " + longLat[(longLat.length-2)]+ " " + longLat[(longLat.length-3)]);

            			// find the rectangle of district
            			Double maxLongitude = Double.parseDouble(longLat[0]);
            			Double maxLatitude = 0.0;
            			Double minLongitude = Double.parseDouble(longLat[0]);
            			Double minLatitude = Double.parseDouble(longLat[1]);
            			
            			
            			for(int i=1;i<longLat.length-1;i++) {
            				if(i%2==0) { // 0 127...(longitude)
            					String[] longitude = longLat[i].split(" ");
            					if(maxLongitude < Double.parseDouble(longitude[1]))
            						maxLongitude = Double.parseDouble(longitude[1]);
            					if(minLongitude > Double.parseDouble(longitude[1]))
            						minLongitude = Double.parseDouble(longitude[1]);
            				} else {
            					if(maxLatitude < Double.parseDouble(longLat[i]))
            						maxLatitude = Double.parseDouble(longLat[i]);
            					if(minLatitude > Double.parseDouble(longLat[i]))
            						minLatitude = Double.parseDouble(longLat[i]);
            				}
            			}
            			System.out.println(maxLongitude + " : " + minLongitude + " \n " + maxLatitude + " : " + minLatitude + " \n ");
            			
            			District district = new District();
            			district.setMaxLat(maxLatitude);
            			district.setMaxLong(maxLongitude);
            			district.setMinLat(minLatitude);
            			district.setMinLong(minLongitude);
            			
                     surfaceArea.add(district);
            			
            		}
            		count ++;

            }
            System.out.println(">>>>>>>>" + count +" "+ surfaceArea);
            
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
