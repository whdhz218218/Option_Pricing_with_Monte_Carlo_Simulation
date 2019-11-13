package path;

import java.time.LocalDate;

/**
 *
 */
public class DataPoint {

    private LocalDate _date;
    private double _price;
    
    public DataPoint() {
    	
    }
   
    public DataPoint(LocalDate date,double price) {
    	_price =price;
    	_date=date;
    }

    public LocalDate date() {
        return _date;
    }

    public DataPoint date(LocalDate date) {
        this._date = date;
        return this;
    }

    public double price() {
        return _price;
    }

    public DataPoint price(double price) {
        this._price = price;
        return this;
    }
}
