package dev.iamtuann.flashlingo.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ChangePasswordRequest {
    @NotBlank(message = "Current password is required")
    @Size(min = 6, max = 20, message = "Current password must be between 6 and 20 characters")
    private String currentPassword;
    @NotBlank(message = "New password is required")
    @Size(min = 6, max = 20, message = "New password must be between 6 and 20 characters")
    private String newPassword;
}
