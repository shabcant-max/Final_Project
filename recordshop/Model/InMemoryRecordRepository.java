package recordshop.model;

import java.util.ArrayList;
import java.util.List;


public class InMemoryRecordRepository implements RecordRepository {

    private final List<Record> records = new ArrayList<>();

    @Override
    public List<Record> findAll() {
        return new ArrayList<>(records);
    }

    @Override
    public Record findById(String id) {
        if (id == null) {
            return null;
        }
        for (Record r : records) {
            if (r.getIdnumber().equals(id)) {
                return r;
            }
        }
        return null;
    }

    @Override
    public Record save(Record record) {
        if (record == null) {
            throw new IllegalArgumentException("Cannot save a null record.");
        }
        for (int i = 0; i < records.size(); i++) {
            if (records.get(i).getIdnumber().equals(record.getIdnumber())) {
                records.set(i, record);
                return record;
            }
        }
        records.add(record);
        return record;
    }

    @Override
    public void deleteById(String id) {
        if (id == null) {
            return;
        }
        records.removeIf(r -> r.getIdnumber().equals(id));
    }
}