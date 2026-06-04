package dasturlash.uz.smsprovider.entity;

import dasturlash.uz.smsprovider.enums.SmsStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "sms_messages")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SmsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long clientId; // yoki relation

    private String phone;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String text;

    @Column(name = "scheduled_date")
    private LocalDateTime scheduledDate;

    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    private SmsStatus status;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdDate;
}