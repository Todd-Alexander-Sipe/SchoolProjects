/*
Written by Todd Sipe February of 2021 for CS351
This class creates a visual representation of the bones
 */

package GUIGame;

import javafx.scene.Group;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class MakeVisual {


    public MakeVisual(HBox hb, int index, int side1, int side2){
        Group g = make(side1, side2);
        hb.getChildren().add(index, g);
    }

    //creates visuals for bones
    private Group make(int num1, int num2){
        Group g = new Group();
        Rectangle r1 = new Rectangle(40, 40, Color.BEIGE);
        r1.setStroke(Color.BLACK);
        r1.setArcHeight(8);
        r1.setArcWidth(8);
        Rectangle r2 = new Rectangle(40, 40, Color.BEIGE);
        r2.setStroke(Color.BLACK);
        r2.setX(40);
        r2.setArcHeight(8);
        r2.setArcWidth(8);
        switch(num1) {
            case 0:
                g.getChildren().addAll(r1);
                break;
            case 1:
                Circle dot1 = new Circle(20,20, 5, Color.SKYBLUE);
                dot1.setStroke(Color.BLACK);
                g.getChildren().addAll(r1, dot1);
                break;
            case 2:
                Circle dot2 = new Circle(10,10, 5, Color.GREEN);
                dot2.setStroke(Color.BLACK);
                Circle dot3 = new Circle(30,30, 5, Color.GREEN);
                dot3.setStroke(Color.BLACK);
                g.getChildren().addAll(r1, dot2, dot3);
                break;
            case 3:
                Circle dot4 = new Circle(10,10, 5, Color.RED);
                dot4.setStroke(Color.BLACK);
                Circle dot5 = new Circle(20,20, 5, Color.RED);
                dot5.setStroke(Color.BLACK);
                Circle dot6 = new Circle(30,30, 5, Color.RED);
                dot6.setStroke(Color.BLACK);
                g.getChildren().addAll(r1, dot4, dot5, dot6);
                break;
            case 4:
                Circle dot7 = new Circle(10,10, 5, Color.BROWN);
                dot7.setStroke(Color.BLACK);
                Circle dot8 = new Circle(30,10, 5, Color.BROWN);
                dot8.setStroke(Color.BLACK);
                Circle dot9 = new Circle(10,30, 5, Color.BROWN);
                dot9.setStroke(Color.BLACK);
                Circle dot10 = new Circle(30,30, 5, Color.BROWN);
                dot10.setStroke(Color.BLACK);
                g.getChildren().addAll(r1, dot7, dot8, dot9, dot10);
                break;
            case 5:
                Circle dot11 = new Circle(10,10, 5, Color.BLUE);
                dot11.setStroke(Color.BLACK);
                Circle dot12 = new Circle(30,10, 5, Color.BLUE);
                dot12.setStroke(Color.BLACK);
                Circle dot13 = new Circle(10,30, 5, Color.BLUE);
                dot13.setStroke(Color.BLACK);
                Circle dot14 = new Circle(30,30, 5, Color.BLUE);
                dot14.setStroke(Color.BLACK);
                Circle dot15 = new Circle(20,20, 5, Color.BLUE);
                dot15.setStroke(Color.BLACK);
                g.getChildren().addAll(r1, dot11, dot12, dot13, dot14, dot15);
                break;
            case 6:
                Circle dot16 = new Circle(10,10, 5, Color.YELLOW);
                dot16.setStroke(Color.BLACK);
                Circle dot17 = new Circle(10,20, 5, Color.YELLOW);
                dot17.setStroke(Color.BLACK);
                Circle dot18 = new Circle(10,30, 5, Color.YELLOW);
                dot18.setStroke(Color.BLACK);
                Circle dot19 = new Circle(30,10, 5, Color.YELLOW);
                dot19.setStroke(Color.BLACK);
                Circle dot20 = new Circle(30,20, 5, Color.YELLOW);
                dot20.setStroke(Color.BLACK);
                Circle dot21 = new Circle(30,30, 5, Color.YELLOW);
                dot21.setStroke(Color.BLACK);
                g.getChildren().addAll(r1, dot16, dot17, dot18, dot19, dot20, dot21);
                break;
            default: break;
        }
        switch(num2) {
            case 0:
                g.getChildren().addAll(r2);
                break;
            case 1:
                Circle dot1 = new Circle(60,20, 5, Color.SKYBLUE);
                dot1.setStroke(Color.BLACK);
                g.getChildren().addAll(r2, dot1);
                break;
            case 2:
                Circle dot2 = new Circle(50,10, 5, Color.GREEN);
                dot2.setStroke(Color.BLACK);
                Circle dot3 = new Circle(70,30, 5, Color.GREEN);
                dot3.setStroke(Color.BLACK);
                g.getChildren().addAll(r2, dot2, dot3);
                break;
            case 3:
                Circle dot4 = new Circle(50,10, 5, Color.RED);
                dot4.setStroke(Color.BLACK);
                Circle dot5 = new Circle(60,20, 5, Color.RED);
                dot5.setStroke(Color.BLACK);
                Circle dot6 = new Circle(70,30, 5, Color.RED);
                dot6.setStroke(Color.BLACK);
                g.getChildren().addAll(r2, dot4, dot5, dot6);
                break;
            case 4:
                Circle dot7 = new Circle(50,10, 5, Color.BROWN);
                dot7.setStroke(Color.BLACK);
                Circle dot8 = new Circle(70,10, 5, Color.BROWN);
                dot8.setStroke(Color.BLACK);
                Circle dot9 = new Circle(50,30, 5, Color.BROWN);
                dot9.setStroke(Color.BLACK);
                Circle dot10 = new Circle(70,30, 5, Color.BROWN);
                dot10.setStroke(Color.BLACK);
                g.getChildren().addAll(r2, dot7, dot8, dot9, dot10);
                break;
            case 5:
                Circle dot11 = new Circle(50,10, 5, Color.BLUE);
                dot11.setStroke(Color.BLACK);
                Circle dot12 = new Circle(70,10, 5, Color.BLUE);
                dot12.setStroke(Color.BLACK);
                Circle dot13 = new Circle(50,30, 5, Color.BLUE);
                dot13.setStroke(Color.BLACK);
                Circle dot14 = new Circle(70,30, 5, Color.BLUE);
                dot14.setStroke(Color.BLACK);
                Circle dot15 = new Circle(60,20, 5, Color.BLUE);
                dot15.setStroke(Color.BLACK);
                g.getChildren().addAll(r2, dot11, dot12, dot13, dot14, dot15);
                break;
            case 6:
                Circle dot16 = new Circle(50,10, 5, Color.YELLOW);
                dot16.setStroke(Color.BLACK);
                Circle dot17 = new Circle(50,20, 5, Color.YELLOW);
                dot17.setStroke(Color.BLACK);
                Circle dot18 = new Circle(50,30, 5, Color.YELLOW);
                dot18.setStroke(Color.BLACK);
                Circle dot19 = new Circle(70,10, 5, Color.YELLOW);
                dot19.setStroke(Color.BLACK);
                Circle dot20 = new Circle(70,20, 5, Color.YELLOW);
                dot20.setStroke(Color.BLACK);
                Circle dot21 = new Circle(70,30, 5, Color.YELLOW);
                dot21.setStroke(Color.BLACK);
                g.getChildren().addAll(r2, dot16, dot17, dot18, dot19, dot20, dot21);
                break;
            default: break;
        }
        return new Group(g);
    }
}
