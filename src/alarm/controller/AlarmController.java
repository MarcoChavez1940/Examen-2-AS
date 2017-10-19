/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package alarm.controller;

import alarm.model.AlarmModel;
import alarm.view.AlarmView;
import alarm.view.AllAlarmsView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 *
 * @author marco
 */
public class AlarmController implements ActionListener{
    ArrayList<AlarmModel> alarms = new ArrayList();
    AlarmView alarmView = new AlarmView();
    AllAlarmsView allAlarmsView = new AllAlarmsView();
    Locale locale;
    ResourceBundle labels;
    static Logger log = Logger.getLogger(AlarmController.class);
    URL url = AlarmController.class.getResource("/properties/Log4j.properties");
    
    public AlarmController(){
        initComponents();
    }
    
    public void initComponents(){
        alarmView.setVisible(true);
        alarmView.createButton.addActionListener(this);
        alarmView.showButton.addActionListener(this);
        allAlarmsView.deleteAlarm.addActionListener(this);
        alarmView.lenguageButton.addActionListener(this);
        allAlarmsView.updateButton.addActionListener(this);
        allAlarmsView.alarmsTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if(allAlarmsView.alarmsTable.getSelectedRow() != -1){
                    int alarmSelected = allAlarmsView.alarmsTable.getSelectedRow();
                    allAlarmsView.updateHour.setText(String.valueOf(allAlarmsView.alarmsTable.getValueAt(alarmSelected, 0)));
                    allAlarmsView.updateMinute.setText(String.valueOf(allAlarmsView.alarmsTable.getValueAt(alarmSelected, 1)));
                }
            }
        });
    }
    
    public void showAllAlarms(){
        DefaultTableModel modeloT = new DefaultTableModel();
        allAlarmsView.alarmsTable.setModel(modeloT);
        
        modeloT.addColumn("Hora");
        modeloT.addColumn("Minutos");
        
        AlarmModel alarm = new AlarmModel();
        Object[] columna = new Object[2];
        for(int i=0; i<alarms.size(); i++){  
            alarm = alarms.get(i);
            
            columna[0] = alarm.getHourAlarm();
            columna[1] = alarm.getMinuteAlarm();

            modeloT.addRow(columna);
        }
        allAlarmsView.setVisible(true);
    }
    
    public void setLenguage(String lenguage){
        locale = new Locale(lenguage);
        getResources();
        alarmView.instructionLabel.setText(labels.getString("instruction"));
        alarmView.hourLabel.setText(labels.getString("hour"));
        alarmView.minuteLabel.setText(labels.getString("minute"));
        alarmView.createButton.setText(labels.getString("createButton"));
        alarmView.showButton.setText(labels.getString("showButton"));
        alarmView.lenguageButton.setText(labels.getString("chanceLenguage"));
        alarmView.lenguageLabel.setText(labels.getString("chanceLenguage"));
        allAlarmsView.deleteAlarm.setText(labels.getString("deleteAlarm"));
        allAlarmsView.chanceLabel.setText(labels.getString("chanceAlarm"));
        allAlarmsView.updateHourLabel.setText(labels.getString("hour"));
        allAlarmsView.updateMinuteLabel.setText(labels.getString("minute"));
        alarmView.formateLabel.setText(labels.getString("formatHour"));
    }
    
    public void getOpcionLenguage(){
        int option = alarmView.lenguageComboBox.getSelectedIndex();
        switch(option){
            case 0:
                setLenguage("es");
                break;
            case 1:
                setLenguage("en");
                break;
            case 2:
                setLenguage("fr");
                break;
        }
    }
    
    public void getResources(){
        labels = ResourceBundle.getBundle("lenguages.lenguage", locale);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == alarmView.createButton){
            try {
                AlarmModel newAlarm = new AlarmModel(Integer.valueOf(alarmView.hourText.getText()), Integer.valueOf(alarmView.minuteText.getText()));
                alarms.add(newAlarm);
                JOptionPane.showMessageDialog(alarmView, "Alarma creada");
                alarmView.hourText.setText("");
                alarmView.minuteText.setText("");
                PropertyConfigurator.configure(url);
                log.info("Se creo una alarma");    
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(alarmView, "Error: Se ingreso letras en la hora y/o minutos");
                PropertyConfigurator.configure(url);
                log.error("Se ingreso letras en la hora y/o minutos"); 
            }

        }
        if(e.getSource() == alarmView.showButton){
            showAllAlarms();
        }
        if(e.getSource() == allAlarmsView.deleteAlarm){
            alarms.get(allAlarmsView.alarmsTable.getSelectedRow()).thread.stop();
            alarms.remove(allAlarmsView.alarmsTable.getSelectedRow());
            showAllAlarms();
            PropertyConfigurator.configure(url);
            log.info("Se elimino una alarma");
        }
        if(e.getSource() == alarmView.lenguageButton){
            getOpcionLenguage();
            PropertyConfigurator.configure(url);
            log.info("Se cambio el idioma");
        }
        if(e.getSource() == allAlarmsView.updateButton){
            alarms.get(allAlarmsView.alarmsTable.getSelectedRow()).thread.stop();
            alarms.remove(allAlarmsView.alarmsTable.getSelectedRow());
            AlarmModel newAlarm = new AlarmModel(Integer.valueOf(allAlarmsView.updateHour.getText()), Integer.valueOf(allAlarmsView.updateMinute.getText()));
            alarms.add(newAlarm);
            showAllAlarms();
            PropertyConfigurator.configure(url);
            log.info("Se modifico una alarma");
        }
    }
    
    
}

