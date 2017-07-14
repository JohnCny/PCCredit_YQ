package test.com.cardpay.pccredit.manager.service.salary; 

import com.cardpay.pccredit.BaseJunitTest;
import com.cardpay.pccredit.manager.dao.comdao.SalaryRecordComdao;
import com.cardpay.pccredit.manager.service.salary.EmployeeSalaryFactoryService;
import com.cardpay.pccredit.manager.service.salary.EmployeeSalaryService;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;


/** 
* EmployeeSalaryFactory Tester. 
* 
* @author <Authors name> 
* @since <pre>���� 10, 2017</pre> 
* @version 1.0 
*/

public class EmployeeSalaryServiceFactoryTest extends BaseJunitTest{



    @Resource
    private EmployeeSalaryFactoryService employeeSalaryFactoryService;

    @Autowired
    private SalaryRecordComdao salaryRecordComdao;

    @Before
    public void setup(){

    }

    @Test
    public void testSalarySum() throws Exception{
        EmployeeSalaryService employeeSalaryService=employeeSalaryFactoryService.createSalaryByUser(1,"2c95ee825c386b84015c387ddfbe0007");
        employeeSalaryService.calculateRealSalary("2017-06");

//        System.out.println(employeeSalaryFactoryService.createSalaryByUser(1,"2c95ee825c386b84015c387ddfbe0007").calculateRealSalary("2017-06"));
    }
} 
