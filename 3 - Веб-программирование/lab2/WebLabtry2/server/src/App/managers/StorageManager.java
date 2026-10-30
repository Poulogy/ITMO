package App.managers;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

public class StorageManager {
    HashMap<String, List<HashMap<String, Object>>> Storage;

    public StorageManager() {
        Storage = new HashMap<>();
    }

    public List<HashMap<String, Object>> getStorage(String id, Logger log){
        List<HashMap<String, Object>> DefaultVal =  new ArrayList<>();
        log.info("Нашли таблицу принадлежащую этой сессии.");
        return Storage.getOrDefault(id, DefaultVal);
    }

    public void putStorage(String id, HashMap<String, Object> table, Logger log){
        if (Storage.containsKey(id)) {
            List<HashMap<String, Object>> storageInfo = Storage.get(id);
            log.info("Получили доступ к уже существующей ячейке хранилища с введённым айди.");
            storageInfo.add(table);
            Storage.replace(id, storageInfo);
            log.info("Добавили таблицу в хранилище данного айди");
        } else {
            List<HashMap<String, Object>> storageInfo =  new ArrayList<>();
            log.info("Создали хранилище для данного айди, т.к раньше его не было");
            storageInfo.add(table);
            Storage.put(id, storageInfo);
            log.info("Добавили таблицу в хранилище");
        }



    }
}
