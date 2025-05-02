package dsaa.lab06;

import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

public class TwoWayCycledOrderedListWithSentinel<E extends Comparable<E>> implements IList<E>{

    private class Element{
        public Element(E e) {
            this.object = e;
        }
        public Element(E e, Element next, Element prev) {
            this.object = e;
            this.next = next;
            this.prev = prev;
        }
        // add element e after this
        public void addAfter(Element elem) {
            Element next = this.next;

            elem.prev = this;
            elem.next = next;

            this.next = elem;
            next.prev = elem;
        }
        // assert it is NOT a sentinel
        public void remove() {
            if (this == sentinel)
                throw new IllegalStateException("Tried to remove a sentinel.");

            this.next.prev = this.prev;
            this.prev.next = this.next;

        }
        E object;
        Element next=null;
        Element prev=null;
    }


    Element sentinel;
    int size;

    private class InnerIterator implements Iterator<E>{

        Element current;

        public InnerIterator() {
            this.current = sentinel;
        }
        @Override
        public boolean hasNext() {
            return current.next != sentinel;
        }

        @Override
        public E next() {
            current = current.next;
            return current.object;
        }
    }

    private class InnerListIterator implements ListIterator<E>{

        Element current;

        public InnerListIterator() {
            this.current = sentinel;
        }
        @Override
        public boolean hasNext() {
            return current.next != sentinel;
        }

        @Override
        public E next() {
            current = current.next;
            return current.object;
        }
        @Override
        public void add(E arg0) {
            throw new UnsupportedOperationException();
        }
        @Override
        public boolean hasPrevious() {
            return current.prev != sentinel;
        }
        @Override
        public int nextIndex() {
            throw new UnsupportedOperationException();
        }
        @Override
        public E previous() {
            current = current.prev;
            return current.object;
        }
        @Override
        public int previousIndex() {
            throw new UnsupportedOperationException();
        }
        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
        @Override
        public void set(E arg0) {
            throw new UnsupportedOperationException();
        }
    }

    public TwoWayCycledOrderedListWithSentinel() {

        this.sentinel = new Element(null);
        this.size = 0;

        this.sentinel.next = sentinel;
        this.sentinel.prev = sentinel;

    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean add(E e) {

        if (isEmpty()) {
            Element element = new Element(e, sentinel, sentinel);
            sentinel.next = sentinel.prev = element;
            size++;
            return true;
        }

        Element current = sentinel;
        Element newElement = new Element(e);

        int c = current.next.object.compareTo(e);

        while (c <= 0) {
            current = current.next;

            if (current.next == sentinel)
                break;

            c = current.next.object.compareTo(e);
        }

        current.addAfter(newElement);
        size++;

        return true;
    }

    private Element getElement(int index) {

        if (isEmpty())
            return null;

        if (index < 0 || index >= size)
            return null;

        Element current = sentinel;

        if (index <= size / 2) {

            for (int i = 0; i <= index; i++)
                current = current.next;

            return current;

        } else {

            for (int i = size - 1; i >= index; i--)
                current = current.prev;

            return current;

        }

    }

    private Element getElement(E obj) {

        Element current = sentinel.next;

        if (current == sentinel)
            return null;

        while (!current.object.equals(obj)) {
            current = current.next;

            if (current == sentinel)
                return null;
        }

        return current;

    }

    @Override
    public void add(int index, E element) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        this.sentinel.prev = sentinel;
        this.sentinel.next = sentinel;
        this.size = 0;
    }

    @Override
    public boolean contains(E element) {
        return getElement(element) != null;
    }

    @Override
    public E get(int index) {

        if (isEmpty())
            throw new NoSuchElementException();

        if (index < 0 || index >= size)
            throw new NoSuchElementException();

        Element element = getElement(index);

        if (element == null)
            throw new NoSuchElementException();

        return element.object;
    }

    @Override
    public E set(int index, E element) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int indexOf(E element) {

        int index = 0;

        Iterator<E> it = this.iterator();

        while (it.hasNext()) {
            if (it.next().equals(element))
                return index;

            index++;
        }


        return -1;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public Iterator<E> iterator() {
        return new InnerIterator();
    }

    @Override
    public ListIterator<E> listIterator() {
        return new InnerListIterator();
    }

    @Override
    public E remove(int index) {

        if (isEmpty())
            throw new NoSuchElementException();

        if (index < 0 || index >= size)
            throw new NoSuchElementException();

        Element element = getElement(index);

        if (element == null)
            throw new NoSuchElementException();

        element.remove();
        size--;

        return element.object;
    }

    @Override
    public boolean remove(E e) {

        Element element = getElement(e);

        if (element == null)
            return false;

        element.remove();
        size--;

        return true;
    }

    @Override
    public int size() {
        return size;
    }

    //@SuppressWarnings("unchecked")
    public void add(TwoWayCycledOrderedListWithSentinel<E> other) {

        if (other == null)
            throw new NullPointerException();

        if (other == this)
            return;

        if (other.isEmpty())
            return;

        Element currentThis = this.sentinel.next;
        Element currentOther = other.sentinel.next;

        while (currentThis.object != null && currentOther.object != null) {

            if (currentThis.object.compareTo(currentOther.object) <= 0) {
                currentThis = currentThis.next;
            } else {
                Element nextOther = currentOther.next;

                currentOther.remove();
                currentThis.prev.addAfter(currentOther);

                currentOther = nextOther;

                this.size++;
                other.size--;
            }

        }

        currentThis = this.sentinel.prev;

        while (!other.isEmpty()) {

            currentOther.remove();
            currentThis.addAfter(currentOther);
            currentOther = other.sentinel.next;
            currentThis = this.sentinel.prev;

            this.size++;
            other.size--;

        }
    }

    //@SuppressWarnings({ "unchecked", "rawtypes" })
    public void removeAll(E e) {

        Element current = sentinel.next;

        while (current != sentinel) {

            if (current.object.equals(e)) {
                current.remove();
                size--;
            }

            current = current.next;

        }

    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();

        for (E e : this) {
            sb.append(e.toString()).append(" ");
        }

        return sb.toString().trim();

    }
}
