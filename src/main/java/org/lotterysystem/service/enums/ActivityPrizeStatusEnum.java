package org.lotterysystem.service.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ActivityPrizeStatusEnum {
    INIT(1,"初始化"),
    COMPLETED(2,"已经被抽取");

    private Integer code;
    private String message;

    public static ActivityPrizeStatusEnum forName(String name) {
        for(ActivityPrizeStatusEnum item : ActivityPrizeStatusEnum.values()) {
            if(item.name().equals(name)) {
                return item;
            }
        }
        return null;
    }
}
