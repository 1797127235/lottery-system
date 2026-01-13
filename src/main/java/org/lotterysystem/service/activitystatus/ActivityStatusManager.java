package org.lotterysystem.service.activitystatus;


import org.lotterysystem.service.dto.ConvertActivityStatusDTO;

public interface ActivityStatusManager {
    void handlerEvent(ConvertActivityStatusDTO convertActivityStatusDTO);
}
