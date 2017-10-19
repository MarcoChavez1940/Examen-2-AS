/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package alarm.model;

import java.util.Calendar;
import java.util.GregorianCalendar;
import javax.swing.JOptionPane;

/**
 *
 * @author marco
 */
public class AlarmModel {
    int hourAlarm;
    int minuteAlarm;
    public Thread thread;

    public AlarmModel(){
        
    }
    
    public AlarmModel(int hour, int minute){
        this.hourAlarm = hour;
        this.minuteAlarm = minute;
        alarmSound();
    }
    
    public void alarmSound(){
        thread = new Thread(){
            public void run(){
                while(true){
                    
                    Calendar calendar = new GregorianCalendar();
                    int hours = calendar.get(Calendar.HOUR_OF_DAY);
                    int minutes = calendar.get(Calendar.MINUTE);
                    if(hours == hourAlarm && minutes == minuteAlarm){
                        JOptionPane.showMessageDialog(null, "Alarma encendida, "
                                + hourAlarm + ":" + minuteAlarm + ", apagar!");
                        break;
                    }
                }
            }
        };
        thread.setPriority(Thread.MIN_PRIORITY);
        thread.start();
    }
    
    public int getHourAlarm() {
        return hourAlarm;
    }

    public void setHourAlarm(int hourAlarm) {
        this.hourAlarm = hourAlarm;
    }

    public int getMinuteAlarm() {
        return minuteAlarm;
    }

    public void setMinuteAlarm(int minuteAlarm) {
        this.minuteAlarm = minuteAlarm;
    }   
    
}
