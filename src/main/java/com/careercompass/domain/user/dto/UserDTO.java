package com.careercompass.domain.user.dto;

import com.careercompass.domain.job.dto.JobDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {

    private long id;
    private String username;
    private int age;
    private String email;
    private JobDTO jobDTO;
}
