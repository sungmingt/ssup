package com.ssup.backend.domain.post.dto;

import com.ssup.backend.domain.interest.Interest;
import com.ssup.backend.domain.language.Language;
import com.ssup.backend.domain.location.Location;

import java.util.List;

public record PostFilterMetadataResponse(
        List<LocationFilterItem> locations,
        List<FilterItem> languages,
        List<FilterItem> interests
) {
    public record FilterItem(
            Long id,
            String name
    ) {
        public static FilterItem from(Long id, String name) {
            return new FilterItem(id, name);
        }
    }

    public record LocationFilterItem(
            Long id,
            String name,
            List<FilterItem> children
    ) {}

    public static PostFilterMetadataResponse of(
            List<Location> locations,
            List<Language> languages,
            List<Interest> interests
    ) {
        List<LocationFilterItem> locationFilterItems = locations.stream()
                .filter(l -> l.getLevel() == 1)
                .map(parent -> new LocationFilterItem(
                        parent.getId(),
                        parent.getName(),
                        locations.stream()
                                .filter(child -> child.getParent() != null && child.getParent().getId().equals(parent.getId()))
                                .map(child -> new FilterItem(child.getId(), child.getName()))
                                .toList()
                )).toList();

        return new PostFilterMetadataResponse(
                locationFilterItems,
                languages.stream().map(l -> FilterItem.from(l.getId(), l.getName())).toList(),
                interests.stream().map(i -> FilterItem.from(i.getId(), i.getName())).toList()
        );
    }
}