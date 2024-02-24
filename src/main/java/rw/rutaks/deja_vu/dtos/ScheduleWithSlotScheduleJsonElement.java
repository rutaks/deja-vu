package rw.rutaks.deja_vu.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ScheduleWithSlotScheduleJsonElement extends BasicScheduleJsonElement {
    private int slots;
}
