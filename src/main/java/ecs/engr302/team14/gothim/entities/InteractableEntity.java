package ecs.engr302.team14.gothim.entities;
import ecs.engr302.team14.gothim.util.Point;

public abstract class InteractableEntity extends Entity{

    public InteractableEntity(String name, Point position){
        super(name, position);
    }

    //Needs to filled out   
    public void interact(){}
}