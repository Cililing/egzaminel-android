package materna.przemek.egzaminel.Interfaces;

public interface OnRWItemClickListener<T> {

    void onClick(T item, int pos);
    boolean onLongClick(T item, int pos);
}
