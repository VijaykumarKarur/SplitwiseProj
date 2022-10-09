package com.splitwise.dtos;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MandatoryParameters {
    private boolean name;
    private boolean phone;
    private boolean password;
    private boolean uid;
    private boolean admin;
    private boolean gid;
    private boolean createdBy;
    private boolean desc;
    private boolean paidBy;
    private boolean amounts;
    private boolean split;
    private boolean splits;
    private List<String> mandatoryParametersList = new ArrayList<>();

    public void setToDefault(){
        mandatoryParametersList.clear();
        name = false;
        phone = false;
        password = false;
        uid = false;
        admin = false;
        gid = false;
        createdBy = false;
        desc = false;
        paidBy = false;
        amounts = false;
        split = false;
        splits = false;
    }

    public boolean validateRegisterParameters(){
        mandatoryParametersList.add("name");
        mandatoryParametersList.add("password");
        mandatoryParametersList.add("phone");
        return this.name && this.password && this.phone;
    }

    public boolean validateUpdateProfileParameters(){
        mandatoryParametersList.add("uid");
        mandatoryParametersList.add("name or password or phone");
        return this.uid && (this.name || this.password || this.phone);
    }

    public boolean validateAddGroupParameters(){
        mandatoryParametersList.add("admin");
        mandatoryParametersList.add("name");
        return this.admin && this.name;
    }

    public boolean validateAddMemberParameters(){
        mandatoryParametersList.add("admin");
        mandatoryParametersList.add("gid");
        mandatoryParametersList.add("uid");
        return this.admin && this.gid && this.uid;
    }

    public boolean validateMyTotalParameters(){
        mandatoryParametersList.add("uid");
        return this.uid;
    }

    public boolean validateHistoryParameters(){
        mandatoryParametersList.add("uid");
        return this.uid;
    }

    public boolean validateGroupsParameters(){
        mandatoryParametersList.add("uid");
        return this.uid;
    }

    public boolean validateExpenseParameters(ExpenseSplit split){
        mandatoryParametersList.add("createdBy");
        mandatoryParametersList.add("uid");
        mandatoryParametersList.add("desc");
        mandatoryParametersList.add("paidBy");
        mandatoryParametersList.add("amounts");
        mandatoryParametersList.add("split");
        if(!split.equals(ExpenseSplit.EQUAL)){
            mandatoryParametersList.add("splits");
            return this.createdBy && this.paidBy && this.uid && this.amounts && this.split && this.splits && this.desc;
        }
        return this.createdBy && this.paidBy && this.uid && this.amounts && this.split && this.desc;
    }

    public boolean validateGroupExpenseParameters(ExpenseSplit split){
        mandatoryParametersList.add("createdBy");
        mandatoryParametersList.add("gid");
        mandatoryParametersList.add("desc");
        mandatoryParametersList.add("paidBy");
        mandatoryParametersList.add("amounts");
        mandatoryParametersList.add("split");
        if(!split.equals(ExpenseSplit.EQUAL)){
            mandatoryParametersList.add("splits");
            return this.createdBy && this.paidBy && this.uid && this.amounts && this.split && this.splits && this.desc;
        }
        return this.createdBy && this.paidBy && this.uid && this.amounts && this.split && this.desc;
    }

    public boolean validateSettleUpParameters(){
        mandatoryParametersList.add("uid");
        return this.uid;
    }

    public boolean validateSettleUpGroupParameters(){
        mandatoryParametersList.add("uid");
        mandatoryParametersList.add("gid");
        return this.uid && this.gid;
    }
}
