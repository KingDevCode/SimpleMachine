package simplemachine.simplemachine.Components;

import org.bukkit.Location;
import simplemachine.simplemachine.Data.Configs;

import static simplemachine.simplemachine.SimpleMachine.machineHashMap;
import static simplemachine.simplemachine.Tools.Functies.*;

public class Machine {

    private ItemGenerator itemGenerator = new ItemGenerator();
    private Collector collector = new Collector(this);
    private Location location = null;

    public Machine() {}
    public Machine(Location location){
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }
    public void setLocation(Location location) {
        this.location = location;
    }

    public ItemGenerator getItemGenerator() {
        return itemGenerator;
    }
    public void setItemGenerator(ItemGenerator itemGenerator) {
        this.itemGenerator = itemGenerator;
    }

    public Collector getCollector() {
        return collector;
    }
    public void setCollector(Collector collector) {
        this.collector = collector;
    }

    public boolean isValid() {
        return machineHashMap.containsKey(getLocation());
    }

    public void remove(){
        Configs.getCustomConfig2().set("Machines." + convertLocationToString(this.getLocation()), null);
        saveData();
        machineHashMap.remove(this.getLocation());
    }

    public static Machine getFromLocation(Location location){
        if (machineHashMap.containsKey(location))return machineHashMap.get(location);
        else return new Machine(location);
    }
    public static Machine getMachineFromCollectorLocation(Location location){
        for (Machine machine : machineHashMap.values()){
            if (compareLocations(machine.getCollector().getLocation(), location))return machine;
        }
        return new Machine();
    }
}
