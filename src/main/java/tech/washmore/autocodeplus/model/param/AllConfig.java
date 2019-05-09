package tech.washmore.autocodeplus.model.param;

import lombok.Data;
import tech.washmore.autocodeplus.model.CustomConfig;
import tech.washmore.autocodeplus.model.SysConfig;

@Data
public class AllConfig {
   private CustomConfig extConfig;
   private SysConfig sysConfig;
}
