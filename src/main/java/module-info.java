/**
 * Module info for the project.
 */
module ija.project.robot {
    requires javafx.graphics;
    requires javafx.controls;
    requires javafx.media;
    requires javafx.base;
    requires javafx.web;
    requires javafx.swing;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires java.logging;
    requires java.desktop;
    requires com.google.gson;

    opens ija.project.robot to javafx.fxml;
    exports ija.project.robot;
    exports ija.project.robot.gui;
    exports ija.project.robot.gui.controllers;
    exports ija.project.robot.gui.logic;
    exports ija.project.robot.gui.interfaces;
    exports ija.project.robot.logic.robots;
    exports ija.project.robot.logic.room;
    exports ija.project.robot.logic.common;
    exports ija.project.robot.maps;
}