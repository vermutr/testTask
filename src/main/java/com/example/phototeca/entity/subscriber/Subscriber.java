package com.example.phototeca.entity.subscriber;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Subscriber {

    @Id
    @Column(name = "CHAT_ID")
    private Long chatId;

    @Column(name = "THRESHOLD_PERCENTAGE")
    private Integer thresholdPercentage;

    @Column(name = "CREATE_TIME")
    private LocalDateTime createTime;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Subscriber that)) return false;
        return Objects.equals(getChatId(), that.getChatId())
                && Objects.equals(getThresholdPercentage(), that.getThresholdPercentage())
                && Objects.equals(getCreateTime(), that.getCreateTime());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getChatId(), getThresholdPercentage(), getCreateTime());
    }

}
