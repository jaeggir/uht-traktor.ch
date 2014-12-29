package ch.uhttraktor.website.persistence.util;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Page<T> {

    private List<T> items;

    private int totalItems;

    private int page;

}
