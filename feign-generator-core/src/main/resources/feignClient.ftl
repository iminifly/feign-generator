package feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * ${feignClassName}
 *
 * @author XGF
 * @date ${.now?string("yyyy-MM-dd HH:mm:ss")}
 */
@FeignClient(name = "${serviceName}", c="${feignClassName?uncap_first}", fallbackFactory = ${feignClassName}FallBackFactory.class)
public interface ${feignClassName} {

<#list methods as m>
  /**
   * ${feignClassName}.${m.name}
   *
<#list m.arguments as a>
   * @param ${a.argName} ${a.argName}
</#list>
   * @return ${m.returnType}
   * @author XGF
   * @date ${.now?string("yyyy-MM-dd HH:mm:ss")}
   */
  @RequestMapping(value = "${url}${m.url}", method = RequestMethod.${m.httpMethod})
  ${m.returnType} ${m.name}(<#list m.arguments as a><#if a_index!=0>, </#if><#list a.annotations as an>@${an} </#list>${a.argType} ${a.argName}</#list>);

</#list>
}
