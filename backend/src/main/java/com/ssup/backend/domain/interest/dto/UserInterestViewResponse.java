package com.ssup.backend.domain.interest.dto;

import com.ssup.backend.domain.interest.Interest;
import com.ssup.backend.domain.interest.UserInterest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserInterestViewResponse {

    private String name;

    public static UserInterestViewResponse of(UserInterest userInterest) {
        Interest interest = userInterest.getInterest();

        return UserInterestViewResponse.builder()
                .name(interest.getName())
                .build();
    }

}
