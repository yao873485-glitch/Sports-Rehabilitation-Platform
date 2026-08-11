<template>
  <el-dialog
    title="健康档案"
    :visible.sync="dialogVisible"
    width="80%"
    :close-on-click-modal="false"
    :before-close="handleClose"
    class="health-record-dialog"
  >
    <el-form
      ref="healthForm"
      :model="form"
      :rules="rules"
      :disabled="readonly"
      label-width="100%"
      label-position="top"
      class="health-form"
    >
      <!-- 一、基本信息（只读显示，数据来自patient表） -->
      <el-divider content-position="left"><h3>一、基本信息</h3></el-divider>

      <el-form-item label="姓名">
        <el-input :value="patientData.name || '-'" disabled />
      </el-form-item>

      <el-form-item label="性别">
        <el-input :value="patientData.gender || '-'" disabled />
      </el-form-item>

      <el-form-item label="年龄">
        <el-input :value="calculatedAge ? calculatedAge + '岁' : '-'" disabled />
      </el-form-item>

      <el-form-item label="出生日期">
        <el-input :value="patientData.birthDate || '-'" disabled />
      </el-form-item>

      <el-form-item label="证件类型">
        <el-input :value="patientData.idCardType || '-'" disabled />
      </el-form-item>

      <el-form-item label="证件号">
        <el-input :value="patientData.idCard || '-'" disabled />
      </el-form-item>

      <el-form-item label="民族">
        <el-input :value="patientData.ethnicity || '-'" disabled />
      </el-form-item>

      <el-form-item label="本人电话">
        <el-input :value="patientData.phone || '-'" disabled />
      </el-form-item>

      <el-form-item label="职业">
        <el-input :value="patientData.occupation || '-'" disabled />
      </el-form-item>

      <!-- 健康档案特有的基本信息 -->
      <el-form-item label="工作单位/学校">
        <el-input v-model="form.workUnit" placeholder="请输入工作单位或学校" />
      </el-form-item>

      <el-form-item label="籍贯">
        <el-input v-model="form.nativePlace" placeholder="请输入籍贯（如：广东省深圳市）" />
      </el-form-item>

      <el-form-item label="出生地">
        <el-input v-model="form.birthPlace" placeholder="请输入出生地" />
      </el-form-item>

      <!-- 紧急联系人 -->
      <el-divider content-position="left"><h4>紧急联系人</h4></el-divider>

      <el-form-item label="紧急联系人姓名">
        <el-input v-model="form.emergencyContactName" placeholder="请输入姓名" />
      </el-form-item>

      <el-form-item label="与本人关系">
        <el-input v-model="form.emergencyContactRelation" placeholder="如：父亲、母亲、配偶" />
      </el-form-item>

      <el-form-item label="紧急联系人电话">
        <el-input v-model="form.emergencyContactPhone" placeholder="请输入电话" />
      </el-form-item>

      <!-- 常驻信息 -->
      <el-divider content-position="left"><h4>常驻信息</h4></el-divider>

      <el-form-item label="常驻类型">
        <el-radio-group v-model="form.residenceType">
          <el-radio label="户籍">户籍（请填写）</el-radio>
          <el-radio label="非户籍">非户籍</el-radio>
        </el-radio-group>
      </el-form-item>

      <el-form-item v-if="form.residenceType === '户籍'" label="请详细填写户籍地址">
        <el-input
          v-model="form.householdAddress"
          type="textarea"
          :rows="2"
          placeholder="请输入完整户籍地址（如省市区街道门牌号）"
        />
      </el-form-item>

      <el-form-item label="常驻地址" prop="residenceAddress">
        <el-input v-model="form.residenceAddress" placeholder="请输入当前实际居住地" />
      </el-form-item>

      <!-- 教育婚姻（职业在patient_detail中已有，此处移除） -->
      <el-divider content-position="left"><h4>教育与婚姻状况</h4></el-divider>

      <el-form-item label="文化程度">
        <el-select v-model="form.educationLevel" placeholder="请选择" style="width: 100%;">
          <el-option label="小学" value="小学" />
          <el-option label="初中" value="初中" />
          <el-option label="高中" value="高中" />
          <el-option label="大专" value="大专" />
          <el-option label="本科" value="本科" />
          <el-option label="硕士" value="硕士" />
          <el-option label="博士" value="博士" />
        </el-select>
      </el-form-item>

      <el-form-item label="婚姻状况">
        <el-radio-group v-model="form.maritalStatus">
          <el-radio label="未婚" @click.native="toggleRadio('maritalStatus', '未婚', $event)">未婚</el-radio>
          <el-radio label="已婚" @click.native="toggleRadio('maritalStatus', '已婚', $event)">已婚</el-radio>
          <el-radio label="丧偶" @click.native="toggleRadio('maritalStatus', '丧偶', $event)">丧偶</el-radio>
          <el-radio label="离异" @click.native="toggleRadio('maritalStatus', '离异', $event)">离异</el-radio>
        </el-radio-group>
      </el-form-item>

      <el-form-item label="医疗费用支付方式">
        <el-radio-group v-model="form.medicalPaymentMethod">
          <div style="display: flex; flex-direction: column; gap: 10px;">
            <el-radio label="城镇职工基本医疗保险" @click.native="toggleRadio('medicalPaymentMethod', '城镇职工基本医疗保险', $event)">城镇职工基本医疗保险</el-radio>
            <el-radio label="城乡居民基本医疗保险" @click.native="toggleRadio('medicalPaymentMethod', '城乡居民基本医疗保险', $event)">城乡居民基本医疗保险</el-radio>
            <el-radio label="医疗救助" @click.native="toggleRadio('medicalPaymentMethod', '医疗救助', $event)">医疗救助</el-radio>
            <el-radio label="商业医疗保险" @click.native="toggleRadio('medicalPaymentMethod', '商业医疗保险', $event)">商业医疗保险</el-radio>
            <el-radio label="公费" @click.native="toggleRadio('medicalPaymentMethod', '公费', $event)">公费</el-radio>
            <el-radio label="自费" @click.native="toggleRadio('medicalPaymentMethod', '自费', $event)">自费</el-radio>
            <el-radio label="其他" @click.native="toggleRadio('medicalPaymentMethod', '其他', $event)">其他（请说明）</el-radio>
          </div>
        </el-radio-group>
      </el-form-item>

      <el-form-item v-if="form.medicalPaymentMethod === '其他'" label="请说明医疗费用支付方式">
        <el-input
          v-model="form.medicalPaymentMethodDetail"
          placeholder="请填写其他医疗费用支付方式"
        />
      </el-form-item>


      <!-- 二、过敏史 -->
      <el-divider content-position="left"><h3>二、过敏史</h3></el-divider>

      <el-form-item label="过敏史">
        <el-checkbox-group v-model="allergyList">
          <div style="display: flex; flex-direction: column; gap: 10px;">
            <el-checkbox label="无">无</el-checkbox>
            <el-checkbox label="药物过敏">药物过敏（例如：青霉素、磺胺、链霉素、其他）</el-checkbox>
            <el-checkbox label="食物过敏">食物过敏</el-checkbox>
            <el-checkbox label="其他过敏">其他过敏（请说明）</el-checkbox>
          </div>
        </el-checkbox-group>
      </el-form-item>

      <el-form-item v-if="allergyList.includes('药物过敏') || allergyList.includes('其他过敏')" label="请说明过敏药物/过敏原">
        <el-input
          v-model="form.allergyDetail"
          type="textarea"
          :rows="3"
          placeholder="请详细说明过敏药物或其他过敏原"
        />
      </el-form-item>

      <!-- 三、暴露史 -->
      <el-divider content-position="left"><h3>三、暴露史</h3></el-divider>

      <el-form-item label="暴露史">
        <el-checkbox-group v-model="exposureList">
          <el-checkbox label="无">无</el-checkbox>
          <el-checkbox label="化学品">化学品</el-checkbox>
          <el-checkbox label="毒物">毒物</el-checkbox>
          <el-checkbox label="射线">射线</el-checkbox>
        </el-checkbox-group>
      </el-form-item>

      <!-- 四、既往史 -->
      <el-divider content-position="left"><h3>四、既往史</h3></el-divider>

      <h4 style="margin: 20px 0 15px 0; font-weight: 600;">疾病</h4>
      <el-form-item label="是否有既往病史">
        <el-radio-group v-model="form.diseaseHistory">
          <el-radio label="无">无</el-radio>
          <el-radio label="有">有（请说明：名称、时间）</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item v-if="form.diseaseHistory === '有'" label="请说明既往病史">
        <el-input
          v-model="form.diseaseDetail"
          type="textarea"
          :rows="3"
          placeholder="请说明既往病史名称及发病时间"
        />
      </el-form-item>

      <h4 style="margin: 20px 0 15px 0; font-weight: 600;">手术</h4>
      <el-form-item label="是否有手术史">
        <el-radio-group v-model="form.surgeryHistory">
          <el-radio label="无">无</el-radio>
          <el-radio label="有">有（请说明：名称、时间）</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item v-if="form.surgeryHistory === '有'" label="请说明手术史">
        <el-input
          v-model="form.surgeryDetail"
          type="textarea"
          :rows="3"
          placeholder="请说明既往手术名称及手术时间"
        />
      </el-form-item>

      <h4 style="margin: 20px 0 15px 0; font-weight: 600;">外伤</h4>
      <el-form-item label="是否有外伤史">
        <el-radio-group v-model="form.traumaHistory">
          <el-radio label="无">无</el-radio>
          <el-radio label="有">有（请说明：名称、时间）</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item v-if="form.traumaHistory === '有'" label="请说明外伤史">
        <el-input
          v-model="form.traumaDetail"
          type="textarea"
          :rows="3"
          placeholder="请说明既往外伤名称及外伤时间"
        />
      </el-form-item>

      <h4 style="margin: 20px 0 15px 0; font-weight: 600;">输血</h4>
      <el-form-item label="是否有输血史">
        <el-radio-group v-model="form.transfusionHistory">
          <el-radio label="无">无</el-radio>
          <el-radio label="有">有（请说明：名称、时间）</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item v-if="form.transfusionHistory === '有'" label="请说明输血史">
        <el-input
          v-model="form.transfusionDetail"
          type="textarea"
          :rows="3"
          placeholder="请说明既往输血原因及输血时间"
        />
      </el-form-item>

      <!-- 五、预防接种史 -->
      <el-divider content-position="left"><h3>五、预防接种史</h3></el-divider>

      <el-form-item label="预防接种史">
        <el-radio-group v-model="form.vaccinationHistory">
          <el-radio label="无">无</el-radio>
          <el-radio label="有">有（请说明：名称、时间）</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item v-if="form.vaccinationHistory === '有'" label="请说明接种情况">
        <el-input
          v-model="form.vaccinationDetail"
          type="textarea"
          :rows="3"
          placeholder="请说明接种名称及接种时间"
        />
      </el-form-item>

      <!-- 六、家族史 -->
      <el-divider content-position="left"><h3>六、家族史</h3></el-divider>

      <h4 style="margin: 20px 0 15px 0; font-weight: 600;">父亲</h4>
      <el-form-item label="父亲疾病">
        <el-checkbox-group v-model="fatherDiseaseList">
          <div style="display: flex; flex-direction: column; gap: 8px;">
            <el-checkbox label="无">无</el-checkbox>
            <el-checkbox label="高血压">高血压</el-checkbox>
            <el-checkbox label="2型糖尿病">2型糖尿病</el-checkbox>
            <el-checkbox label="脑卒中">脑卒中</el-checkbox>
            <el-checkbox label="冠心病">冠心病</el-checkbox>
            <el-checkbox label="脑血管病后遗症">脑血管病后遗症</el-checkbox>
            <el-checkbox label="慢性阻塞性肺疾病">慢性阻塞性肺疾病</el-checkbox>
            <el-checkbox label="哮喘">哮喘</el-checkbox>
            <el-checkbox label="尿毒症">尿毒症</el-checkbox>
            <el-checkbox label="恶性肿瘤">恶性肿瘤（请说明）</el-checkbox>
            <el-checkbox label="严重精神病">严重精神病</el-checkbox>
            <el-checkbox label="地方病">地方病（请说明）</el-checkbox>
            <el-checkbox label="职业病">职业病（请说明）</el-checkbox>
            <el-checkbox label="失能">失能</el-checkbox>
            <el-checkbox label="失智">失智</el-checkbox>
            <el-checkbox label="先天畸形">先天畸形（请说明）</el-checkbox>
          </div>
        </el-checkbox-group>
      </el-form-item>
      <el-form-item v-if="needFatherDetail" label="请说明详细情况">
        <el-input
          v-model="form.fatherDetail"
          type="textarea"
          :rows="2"
          placeholder="如果有勾选恶性肿瘤、地方病、职业病、先天畸形请说明"
        />
      </el-form-item>

      <h4 style="margin: 20px 0 15px 0; font-weight: 600;">母亲</h4>
      <el-form-item label="母亲疾病">
        <el-checkbox-group v-model="motherDiseaseList">
          <div style="display: flex; flex-direction: column; gap: 8px;">
            <el-checkbox label="无">无</el-checkbox>
            <el-checkbox label="高血压">高血压</el-checkbox>
            <el-checkbox label="2型糖尿病">2型糖尿病</el-checkbox>
            <el-checkbox label="脑卒中">脑卒中</el-checkbox>
            <el-checkbox label="冠心病">冠心病</el-checkbox>
            <el-checkbox label="脑血管病后遗症">脑血管病后遗症</el-checkbox>
            <el-checkbox label="慢性阻塞性肺疾病">慢性阻塞性肺疾病</el-checkbox>
            <el-checkbox label="哮喘">哮喘</el-checkbox>
            <el-checkbox label="尿毒症">尿毒症</el-checkbox>
            <el-checkbox label="恶性肿瘤">恶性肿瘤（请说明）</el-checkbox>
            <el-checkbox label="严重精神病">严重精神病</el-checkbox>
            <el-checkbox label="地方病">地方病（请说明）</el-checkbox>
            <el-checkbox label="职业病">职业病（请说明）</el-checkbox>
            <el-checkbox label="失能">失能</el-checkbox>
            <el-checkbox label="失智">失智</el-checkbox>
            <el-checkbox label="先天畸形">先天畸形（请说明）</el-checkbox>
          </div>
        </el-checkbox-group>
      </el-form-item>
      <el-form-item v-if="needMotherDetail" label="请说明详细情况">
        <el-input
          v-model="form.motherDetail"
          type="textarea"
          :rows="2"
          placeholder="如果有勾选恶性肿瘤、地方病、职业病、先天畸形请说明"
        />
      </el-form-item>

      <h4 style="margin: 20px 0 15px 0; font-weight: 600;">兄弟姐妹</h4>
      <el-form-item label="兄弟姐妹疾病">
        <el-checkbox-group v-model="siblingsDiseaseList">
          <div style="display: flex; flex-direction: column; gap: 8px;">
            <el-checkbox label="无">无</el-checkbox>
            <el-checkbox label="高血压">高血压</el-checkbox>
            <el-checkbox label="2型糖尿病">2型糖尿病</el-checkbox>
            <el-checkbox label="脑卒中">脑卒中</el-checkbox>
            <el-checkbox label="冠心病">冠心病</el-checkbox>
            <el-checkbox label="脑血管病后遗症">脑血管病后遗症</el-checkbox>
            <el-checkbox label="慢性阻塞性肺疾病">慢性阻塞性肺疾病</el-checkbox>
            <el-checkbox label="哮喘">哮喘</el-checkbox>
            <el-checkbox label="尿毒症">尿毒症</el-checkbox>
            <el-checkbox label="恶性肿瘤">恶性肿瘤（请说明）</el-checkbox>
            <el-checkbox label="严重精神病">严重精神病</el-checkbox>
            <el-checkbox label="地方病">地方病（请说明）</el-checkbox>
            <el-checkbox label="职业病">职业病（请说明）</el-checkbox>
            <el-checkbox label="失能">失能</el-checkbox>
            <el-checkbox label="失智">失智</el-checkbox>
            <el-checkbox label="先天畸形">先天畸形（请说明）</el-checkbox>
          </div>
        </el-checkbox-group>
      </el-form-item>
      <el-form-item v-if="needSiblingsDetail" label="请说明详细情况">
        <el-input
          v-model="form.siblingsDetail"
          type="textarea"
          :rows="2"
          placeholder="如果有勾选恶性肿瘤、地方病、职业病、先天畸形请说明"
        />
      </el-form-item>

      <h4 style="margin: 20px 0 15px 0; font-weight: 600;">子女</h4>
      <el-form-item label="子女疾病">
        <el-checkbox-group v-model="childrenDiseaseList">
          <div style="display: flex; flex-direction: column; gap: 8px;">
            <el-checkbox label="无">无</el-checkbox>
            <el-checkbox label="2型糖尿病">2型糖尿病</el-checkbox>
            <el-checkbox label="脑卒中">脑卒中</el-checkbox>
            <el-checkbox label="冠心病">冠心病</el-checkbox>
            <el-checkbox label="脑血管病后遗症">脑血管病后遗症</el-checkbox>
            <el-checkbox label="慢性阻塞性肺疾病">慢性阻塞性肺疾病</el-checkbox>
            <el-checkbox label="哮喘">哮喘</el-checkbox>
            <el-checkbox label="尿毒症">尿毒症</el-checkbox>
            <el-checkbox label="恶性肿瘤">恶性肿瘤（请说明）</el-checkbox>
            <el-checkbox label="严重精神病">严重精神病</el-checkbox>
            <el-checkbox label="地方病">地方病（请说明）</el-checkbox>
            <el-checkbox label="职业病">职业病（请说明）</el-checkbox>
            <el-checkbox label="失能">失能</el-checkbox>
            <el-checkbox label="失智">失智</el-checkbox>
            <el-checkbox label="先天畸形">先天畸形（请说明）</el-checkbox>
          </div>
        </el-checkbox-group>
      </el-form-item>
      <el-form-item v-if="needChildrenDetail" label="请说明详细情况">
        <el-input
          v-model="form.childrenDetail"
          type="textarea"
          :rows="2"
          placeholder="如果有勾选恶性肿瘤、地方病、职业病、先天畸形请说明"
        />
      </el-form-item>

      <!-- 七、遗传病史 -->
      <el-divider content-position="left"><h3>七、遗传病史</h3></el-divider>

      <el-form-item label="遗传病史">
        <el-radio-group v-model="form.geneticDiseaseHistory">
          <el-radio label="无">无</el-radio>
          <el-radio label="有">有（请说明：疾病名称）</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item v-if="form.geneticDiseaseHistory === '有'" label="请说明疾病名称">
        <el-input
          v-model="form.geneticDiseaseDetail"
          placeholder="请说明疾病名称"
        />
      </el-form-item>

      <!-- 八、残疾情况 -->
      <el-divider content-position="left"><h3>八、残疾情况</h3></el-divider>

      <el-form-item label="残疾情况">
        <el-checkbox-group v-model="disabilityList">
          <div style="display: flex; flex-direction: column; gap: 8px;">
            <el-checkbox label="无残疾">无残疾</el-checkbox>
            <el-checkbox label="视力残疾">视力残疾</el-checkbox>
            <el-checkbox label="听力残疾">听力残疾</el-checkbox>
            <el-checkbox label="言语残疾">言语残疾</el-checkbox>
            <el-checkbox label="肢体残疾">肢体残疾</el-checkbox>
            <el-checkbox label="智力残疾">智力残疾</el-checkbox>
            <el-checkbox label="精神残疾">精神残疾</el-checkbox>
            <el-checkbox label="其他残疾">其他残疾（请说明）</el-checkbox>
          </div>
        </el-checkbox-group>
      </el-form-item>
      <el-form-item v-if="disabilityList.includes('其他残疾')" label="请说明其他残疾">
        <el-input
          v-model="form.disabilityDetail"
          placeholder="请说明其他残疾情况"
        />
      </el-form-item>

      <!-- 九、个人健康标识 -->
      <el-divider content-position="left"><h3>九、个人健康标识</h3></el-divider>

      <el-form-item label="个人健康标识">
        <el-radio-group v-model="form.healthIdentity">
          <el-radio label="0-6岁儿童" @click.native="toggleRadio('healthIdentity', '0-6岁儿童', $event)">0-6岁儿童</el-radio>
          <el-radio label="65岁及以上老年人" @click.native="toggleRadio('healthIdentity', '65岁及以上老年人', $event)">65岁及以上老年人</el-radio>
          <el-radio label="孕产妇" @click.native="toggleRadio('healthIdentity', '孕产妇', $event)">孕产妇</el-radio>
        </el-radio-group>
      </el-form-item>

      <el-form-item v-if="form.healthIdentity === '孕产妇'" label="孕产妇风险等级评估">
        <el-radio-group v-model="form.pregnancyRiskLevel">
          <el-radio label="低风险">低风险</el-radio>
          <el-radio label="一般风险">一般风险</el-radio>
          <el-radio label="较高风险">较高风险</el-radio>
          <el-radio label="高风险">高风险</el-radio>
        </el-radio-group>
      </el-form-item>

      <!-- 慢性/重点疾病 -->
      <el-divider content-position="left"><h3>慢性/重点疾病</h3></el-divider>

      <el-form-item label="慢性/重点疾病">
        <el-checkbox-group v-model="chronicDiseaseList">
          <el-checkbox label="无">无</el-checkbox>
          <el-checkbox label="高血压">高血压</el-checkbox>
          <el-checkbox label="2型糖尿病">2型糖尿病</el-checkbox>
          <el-checkbox label="脑卒中">脑卒中</el-checkbox>
          <el-checkbox label="冠心病">冠心病</el-checkbox>
        </el-checkbox-group>
      </el-form-item>

      <!-- 法定传染病 -->
      <el-divider content-position="left"><h3>法定传染病</h3></el-divider>

      <el-form-item label="法定传染病">
        <el-checkbox-group v-model="infectiousDiseaseList">
          <div style="display: flex; flex-direction: column; gap: 8px;">
            <el-checkbox label="无">无</el-checkbox>
            <el-checkbox label="肺结核">肺结核</el-checkbox>
            <el-checkbox label="肝炎">肝炎</el-checkbox>
            <el-checkbox label="其他法定传染病">其他法定传染病（请说明）</el-checkbox>
          </div>
        </el-checkbox-group>
      </el-form-item>
      <el-form-item v-if="infectiousDiseaseList.includes('其他法定传染病')" label="请说明其他传染病">
        <el-input
          v-model="form.infectiousDiseaseDetail"
          placeholder="如果有其他法定传染病请说明"
        />
      </el-form-item>

      <!-- 体重情况 -->
      <el-divider content-position="left"><h3>体重情况</h3></el-divider>

      <el-form-item label="体重情况">
        <el-radio-group v-model="form.weightStatus">
          <el-radio label="低" @click.native="toggleRadio('weightStatus', '低', $event)">低</el-radio>
          <el-radio label="正常" @click.native="toggleRadio('weightStatus', '正常', $event)">正常</el-radio>
          <el-radio label="超重" @click.native="toggleRadio('weightStatus', '超重', $event)">超重</el-radio>
          <el-radio label="肥胖" @click.native="toggleRadio('weightStatus', '肥胖', $event)">肥胖</el-radio>
        </el-radio-group>
      </el-form-item>

      <!-- 血型 -->
      <el-divider content-position="left"><h3>血型</h3></el-divider>

      <el-form-item label="血型（ABO血型）">
        <el-radio-group v-model="form.bloodTypeAbo">
          <el-radio label="A" @click.native="toggleRadio('bloodTypeAbo', 'A', $event)">A</el-radio>
          <el-radio label="B" @click.native="toggleRadio('bloodTypeAbo', 'B', $event)">B</el-radio>
          <el-radio label="O" @click.native="toggleRadio('bloodTypeAbo', 'O', $event)">O</el-radio>
          <el-radio label="AB" @click.native="toggleRadio('bloodTypeAbo', 'AB', $event)">AB</el-radio>
        </el-radio-group>
      </el-form-item>

      <el-form-item label="Rh血型">
        <el-radio-group v-model="form.bloodTypeRh">
          <el-radio label="Rh阴性" @click.native="toggleRadio('bloodTypeRh', 'Rh阴性', $event)">Rh阴性</el-radio>
          <el-radio label="Rh阳性" @click.native="toggleRadio('bloodTypeRh', 'Rh阳性', $event)">Rh阳性</el-radio>
          <el-radio label="不详" @click.native="toggleRadio('bloodTypeRh', '不详', $event)">不详</el-radio>
        </el-radio-group>
      </el-form-item>

      <!-- 提交按钮 -->
      <el-form-item style="margin-top: 40px; text-align: center;">
        <el-button @click="handleClose">取消</el-button>
        <el-button v-if="!readonly" type="primary" :loading="submitLoading" @click="handleSubmit">提交</el-button>
      </el-form-item>
    </el-form>
  </el-dialog>
</template>

<script>
import { saveHealthRecord, getHealthRecordByPatientId } from '@/api/health-record'

export default {
  name: 'HealthRecordDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    readonly: {
      type: Boolean,
      default: false
    },
    patientData: {
      type: Object,
      default: () => ({})
    },
    patientId: {
      type: [Number, String],
      default: null
    }
  },
  data() {
    return {
      dialogVisible: false,
      submitLoading: false,
      // 复选框列表
      allergyList: [],
      exposureList: [],
      fatherDiseaseList: [],
      motherDiseaseList: [],
      siblingsDiseaseList: [],
      childrenDiseaseList: [],
      disabilityList: [],
      chronicDiseaseList: [],
      infectiousDiseaseList: [],
      form: {
        // 健康档案特有基本信息（name、gender、birthDate、idCard等在patient表中）
        workUnit: '',
        nativePlace: '',
        birthPlace: '',
        // 紧急联系人
        emergencyContactName: '',
        emergencyContactRelation: '',
        emergencyContactPhone: '',
        // 常驻信息
        residenceType: '',
        householdAddress: '',
        residenceAddress: '',
        // 教育婚姻（occupation在patient_detail表中）
        educationLevel: '',
        maritalStatus: '',
        medicalPaymentMethod: '',
        medicalPaymentMethodDetail: '',
        // 过敏史
        allergyDetail: '',
        // 既往史
        diseaseHistory: '无',
        diseaseDetail: '',
        surgeryHistory: '无',
        surgeryDetail: '',
        traumaHistory: '无',
        traumaDetail: '',
        transfusionHistory: '无',
        transfusionDetail: '',
        // 预防接种史
        vaccinationHistory: '无',
        vaccinationDetail: '',
        // 家族史详情
        fatherDetail: '',
        motherDetail: '',
        siblingsDetail: '',
        childrenDetail: '',
        // 遗传病史
        geneticDiseaseHistory: '无',
        geneticDiseaseDetail: '',
        // 残疾情况
        disabilityDetail: '',
        // 个人健康标识
        healthIdentity: '',
        pregnancyRiskLevel: '',
        // 法定传染病
        infectiousDiseaseDetail: '',
        // 体重血型
        weightStatus: '',
        bloodTypeAbo: '',
        bloodTypeRh: ''
      },
      rules: {
        // 只保留健康档案特有字段的验证（基本信息验证在patient表单中）
        residenceAddress: [{ required: true, message: '请输入常驻地址', trigger: 'blur' }]
      }
    }
  },
  computed: {
    /** 计算年龄（根据患者出生日期） */
    calculatedAge() {
      if (!this.patientData.birthDate) return null
      const birthDate = new Date(this.patientData.birthDate)
      const today = new Date()
      let age = today.getFullYear() - birthDate.getFullYear()
      const monthDiff = today.getMonth() - birthDate.getMonth()
      if (monthDiff < 0 || (monthDiff === 0 && today.getDate() < birthDate.getDate())) {
        age--
      }
      return age
    },
    /** 是否需要填写父亲疾病详情 */
    needFatherDetail() {
      return this.fatherDiseaseList.some(item =>
        ['恶性肿瘤', '地方病', '职业病', '先天畸形'].includes(item)
      )
    },
    /** 是否需要填写母亲疾病详情 */
    needMotherDetail() {
      return this.motherDiseaseList.some(item =>
        ['恶性肿瘤', '地方病', '职业病', '先天畸形'].includes(item)
      )
    },
    /** 是否需要填写兄弟姐妹疾病详情 */
    needSiblingsDetail() {
      return this.siblingsDiseaseList.some(item =>
        ['恶性肿瘤', '地方病', '职业病', '先天畸形'].includes(item)
      )
    },
    /** 是否需要填写子女疾病详情 */
    needChildrenDetail() {
      return this.childrenDiseaseList.some(item =>
        ['恶性肿瘤', '地方病', '职业病', '先天畸形'].includes(item)
      )
    }
  },
  watch: {
    visible(val) {
      console.log('HealthRecordDialog watch visible:', val)
      console.log('patientId:', this.patientId)
      this.dialogVisible = val
      if (val) {
        this.initFormData()
      }
    },
    dialogVisible(val) {
      console.log('HealthRecordDialog watch dialogVisible:', val)
      this.$emit('update:visible', val)
    }
  },
  methods: {
    toggleRadio(field, value, event) {
      if (this.readonly) return
      if (this.form[field] === value) {
        if (event) {
          event.preventDefault()
          event.stopPropagation()
        }
        this.form[field] = ''
        if (field == 'medicalPaymentMethod') {
          this.form.medicalPaymentMethodDetail = ''
        }
        if (field == 'healthIdentity') {
          this.form.pregnancyRiskLevel = ''
        }
      }
    },
    /** 初始化表单数据 */
    async initFormData() {
      // 患者基本信息（姓名、性别、出生日期等）来自 patientData props，
      // 只读显示，不需要初始化到 form 中

      // 如果有患者ID，从后端加载健康档案数据
      if (this.patientId) {
        try {
          const response = await getHealthRecordByPatientId(this.patientId)
          if (response && response.data) {
            const data = response.data

            // 填充表单数据
            this.form = {
              workUnit: data.workUnit || '',
              nativePlace: data.nativePlace || '',
              birthPlace: data.birthPlace || '',
              emergencyContactName: data.emergencyContactName || '',
              emergencyContactRelation: data.emergencyContactRelation || '',
              emergencyContactPhone: data.emergencyContactPhone || '',
              residenceType: data.residenceType || '',
              householdAddress: data.householdAddress || '',
              residenceAddress: data.residenceAddress || '',
              educationLevel: data.educationLevel || '',
              maritalStatus: data.maritalStatus || '',
              medicalPaymentMethod: data.medicalPaymentMethod || '',
              medicalPaymentMethodDetail: data.medicalPaymentMethodDetail || '',
              allergyDetail: data.allergyDetail || '',
              diseaseHistory: data.diseaseHistory || '',
              diseaseDetail: data.diseaseDetail || '',
              surgeryHistory: data.surgeryHistory || '',
              surgeryDetail: data.surgeryDetail || '',
              traumaHistory: data.traumaHistory || '',
              traumaDetail: data.traumaDetail || '',
              transfusionHistory: data.transfusionHistory || '',
              transfusionDetail: data.transfusionDetail || '',
              vaccinationHistory: data.vaccinationHistory || '',
              vaccinationDetail: data.vaccinationDetail || '',
              geneticDiseaseHistory: data.geneticDiseaseHistory || '',
              geneticDiseaseDetail: data.geneticDiseaseDetail || '',
              fatherDetail: data.fatherDetail || '',
              motherDetail: data.motherDetail || '',
              siblingsDetail: data.siblingsDetail || '',
              childrenDetail: data.childrenDetail || '',
              disabilityDetail: data.disabilityDetail || '',
              healthIdentity: data.healthIdentity || '',
              pregnancyRiskLevel: data.pregnancyRiskLevel || '',
              infectiousDiseaseDetail: data.infectiousDiseaseDetail || '',
              weightStatus: data.weightStatus || '',
              bloodTypeAbo: data.bloodTypeAbo || '',
              bloodTypeRh: data.bloodTypeRh || ''
            }

            // 填充复选框列表数据
            this.allergyList = data.allergyList || []
            this.exposureList = data.exposureList || []
            this.fatherDiseaseList = data.fatherDiseaseList || []
            this.motherDiseaseList = data.motherDiseaseList || []
            this.siblingsDiseaseList = data.siblingsDiseaseList || []
            this.childrenDiseaseList = data.childrenDiseaseList || []
            this.disabilityList = data.disabilityList || []
            this.chronicDiseaseList = data.chronicDiseaseList || []
            this.infectiousDiseaseList = data.infectiousDiseaseList || []
          }
        } catch (error) {
          console.error('加载健康档案数据失败:', error)
          // 如果查询失败（比如还没有健康档案），不显示错误，允许用户新建
        }
      }
    },

    /** 提交表单 */
    handleSubmit() {
      if (this.readonly) return
      this.$refs.healthForm.validate(valid => {
        if (valid) {
          this.submitLoading = true

          // 准备提交数据
          const submitData = {
            patientId: this.patientId,
            ...this.form,
            // 将复选框列表添加到提交数据
            allergyList: this.allergyList,
            exposureList: this.exposureList,
            fatherDiseaseList: this.fatherDiseaseList,
            motherDiseaseList: this.motherDiseaseList,
            siblingsDiseaseList: this.siblingsDiseaseList,
            childrenDiseaseList: this.childrenDiseaseList,
            disabilityList: this.disabilityList,
            chronicDiseaseList: this.chronicDiseaseList,
            infectiousDiseaseList: this.infectiousDiseaseList
          }

          // 调用保存API
          saveHealthRecord(submitData)
            .then(response => {
              this.$message.success('健康档案保存成功')
              this.$emit('saved', response)
              this.dialogVisible = false
            })
            .catch(error => {
              this.$message.error('保存失败: ' + (error.message || '未知错误'))
            })
            .finally(() => {
              this.submitLoading = false
            })
        } else {
          this.$message.error('请完善必填项')
          return false
        }
      })
    },

    /** 关闭对话框 */
    handleClose() {
      this.$confirm('确定关闭吗？未保存的数据将丢失', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.dialogVisible = false
      }).catch(() => {})
    }
  }
}
</script>

<style lang="scss" scoped>
.health-record-dialog {
  ::v-deep .el-dialog__body {
    max-height: 70vh;
    overflow-y: auto;
  }
}

.health-form {
  h3 {
    margin: 0;
    font-size: 16px;
    color: #303133;
  }

  h4 {
    margin: 0;
    font-size: 14px;
    color: #606266;
  }

  ::v-deep .el-divider__text {
    background-color: #fff;
  }

  // 标签在上方，左对齐
  ::v-deep .el-form-item__label {
    text-align: left !important;
    padding-bottom: 8px;
    padding-right: 0;
    font-weight: 500;
    color: #606266;
  }

  // 输入框和选项左对齐
  ::v-deep .el-form-item__content {
    text-align: left;
  }

  // 单选框和复选框左对齐
  ::v-deep .el-radio-group,
  ::v-deep .el-checkbox-group {
    display: flex;
    flex-direction: column;
    align-items: flex-start;
  }

  // 单选/复选项之间的间距
  ::v-deep .el-radio,
  ::v-deep .el-checkbox {
    margin-right: 30px;
    margin-bottom: 10px;
  }

  // 专门设置按钮区域居中
  ::v-deep .el-form-item[style*="text-align: center"] .el-form-item__content {
    text-align: center !important;
  }
}

.info-item {
  margin-bottom: 10px;

  .label {
    color: #606266;
    font-weight: 500;
  }

  .value {
    color: #303133;
  }
}
</style>
