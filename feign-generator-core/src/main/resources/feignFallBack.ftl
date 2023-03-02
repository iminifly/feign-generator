package feign.fallback;

import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import lombok.extern.slf4j.Slf4j;

/**
 * ${feignClassName}FallBackFactory
 *
 * @author XGF
 * @date ${.now?string("yyyy-MM-dd HH:mm:ss")}
 */
@Slf4j
@Component
public class ${feignClassName}FallBackFactory implements FallbackFactory<${feignClassName}> {

  @Override
  public ${feignClassName} create(Throwable throwable) {

    return new ${feignClassName}() {

<#list methods as m>
      @Override
      public ${m.returnType} ${m.name}(<#list m.arguments as a><#if a_index!=0>, </#if><#list a.annotations as an>@${an} </#list>${a.argType} ${a.argName}</#list>) {
        log.error("${feignClassName}.${m.name}", throwable);
        return null;
      }

</#list>
    };
  }
}
