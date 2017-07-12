package dd.final_report_02_20151046;

/**
 * Created by jin on 2017-06-26.
 */

public class MyDiaryData {
    private String title;
    private String date;
    private String context;
    private int  _id;

    public MyDiaryData(int  _id, String Title, String Date, String Context) {
        this._id = _id;
        this.title = Title;
        this.date = Date;
        this.context = Context;
    }

    public void set_id(int  _id) {
        this._id = _id;
    }

    public int  get_id() {
        return _id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getContext() {
        return context;
    }

    public void setWhich(String Context) {
        this.context = Context;
    }
}
