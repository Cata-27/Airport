package core.models.storage;

public interface PrototypeCloneable<T> {
    T clone(T original); // Método para clonar objetos de tipo T
}