package ${packageName};

import lombok.Data;

/**
 * ${modelName}
 *
 * @author XGF
 * @date ${.now?string("yyyy-MM-dd HH:mm:ss")}
 */
@Data
public class ${modelName} {

<#list modelFields as f>
   /**
    * ${f.name}
    */
   private ${f.type} ${f.name};

</#list>
}
