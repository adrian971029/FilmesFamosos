package com.adrian971029.filmesfamosos.data;

import java.util.List;

public interface SQLiteDAO<T> {

    public long inserir(T t) throws Exception;
    public void atualizar(T t) throws Exception;
    public boolean deletar(T t,String id) throws Exception;
    public T buscar(long id);
    public List<T> buscarTodos();

}
