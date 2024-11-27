package org.lepigslayer.outbreakZ.Infection;

import org.bukkit.plugin.Plugin;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class InfectionLoader {

    static HashMap<UUID,Integer> loadTurnDates(Plugin plugin){
        HashMap<UUID,Integer> turnDates = (HashMap<UUID, Integer>) loadFromFile("liveTurnDates.dat",plugin);
        if(turnDates==null)
            return new HashMap<>();
        return turnDates;
    }

    static HashMap<UUID,InfectionState> loadInfectionStates(Plugin plugin){
        HashMap<UUID, InfectionState> states = (HashMap<UUID, InfectionState>) loadFromFile("liveInfectionStates.dat",plugin);
        if(states==null)
            return new HashMap<>();
        return states;
    }

    static void saveTurnDates(Plugin plugin, Map<UUID,Integer> turnDates){
        saveToFile("liveTurnDates.dat",turnDates,plugin);
    }

    static void saveInfectionStates(Plugin plugin, Map<UUID,InfectionState> states){
        saveToFile("liveInfectionStates.dat",states,plugin);
    }

    private static void saveToFile(String fileName, Object obj, Plugin plugin){

        File folder = plugin.getDataFolder();

        if(!folder.exists()){
            folder.mkdir();
        }

        File file = new File(folder, fileName);

        if(file.exists()){
            try{
                FileOutputStream fis = new FileOutputStream(file);

                ObjectOutputStream ois = new ObjectOutputStream(fis);
                ois.writeObject(obj);
                ois.close();
            }catch(Exception ignored){
                throw new RuntimeException("File Write Error");
            }
        }
    }

    private static Object loadFromFile(String fileName,Plugin plugin){
        File folder = plugin.getDataFolder();

        if(!folder.exists()){
            folder.mkdir();
        }

        File file = new File(folder, fileName);

        Object output = null;

        if(file.exists()){
            try{
                FileInputStream fis = new FileInputStream(file);
                byte[] data = new byte[(int)file.length()];
                fis.read(data);
                fis.close();

                ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data));
                output = ois.readObject();
                ois.close();
            }catch(Exception ignored){
                throw new RuntimeException("File Read Error");
            }
        }
        return output;
    }
}
