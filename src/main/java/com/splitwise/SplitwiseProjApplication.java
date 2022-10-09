package com.splitwise;

import com.splitwise.controller.GroupController;
import com.splitwise.controller.UserController;
import com.splitwise.dtos.*;
import com.splitwise.exception.*;
import com.splitwise.models.Group;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class SplitwiseProjApplication implements CommandLineRunner {
    @Autowired
    private UserController userController;

    @Autowired
    private GroupController groupController;

    public static void main(String[] args) {
        SpringApplication.run(SplitwiseProjApplication.class, args);
    }
    private MandatoryParameters mandatoryParameters = new MandatoryParameters();

    @Override
    public void run(String... args) {
        while (true) {
            try{
                System.out.print("splitwise$: ");
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                String input = reader.readLine();
                if(input.equalsIgnoreCase("EXIT")){
                    break;
                }

                String[] parseCommand = input.split("-");
                Commands command = Commands.valueOf(parseCommand[0].trim().toUpperCase());
                mandatoryParameters.setToDefault();
                switch (command){
                    case REGISTER:
                        handleRegisterUser(parseCommand);
                        break;
                    case UPDATEPROFILE:
                        handleUpdateProfile(parseCommand);
                        break;
                    case ADDGROUP:
                        handleAddGroup(parseCommand);
                        break;
                    case ADDMEMBER:
                        handleAddMember(parseCommand);
                        break;
                    case MYTOTAL:
                        handleUserTotal(parseCommand);
                        break;
                    case HISTORY:
                        handleUserHistory(parseCommand);
                        break;
                    case GROUPS:
                        handleUserGroups(parseCommand);
                        break;
                    case EXPENSE:
                        handleAddExpense(parseCommand);
                        break;
                    case GROUPEXPENSE:
                        handleAddGroupExpense(parseCommand);
                        break;
                    case SETTLEUP:
                        handleSettleUp(parseCommand);
                        break;
                    case SETTLEUPGROUP:
                        handleSettleUpGroup(parseCommand);
                        break;
                    default:
                        throw new InvalidCommandException(parseCommand[0].trim());
                }
            }
            catch (IllegalArgumentException illegalArgumentException){
                System.out.println("Illegal Command");
            }
            catch(Exception exception){
                System.out.println(exception.getMessage());
            }

        }
    }

    public void handleRegisterUser(String[] parseCommand) throws InvalidParameterException, MandatoryParametersNotFoundException {
        RegisterUserRequestDTO requestDTO = new RegisterUserRequestDTO();
        for(int index = 1; index < parseCommand.length; index++){
            String[] params = parseCommand[index].trim().split(" ");
            if(params[0].equalsIgnoreCase("NAME")){
                requestDTO.setName(params[1].trim());
                mandatoryParameters.setName(true);
            }
            else if(params[0].equalsIgnoreCase("PHONE")){
                requestDTO.setPhoneNo(params[1].trim());
                mandatoryParameters.setPhone(true);

            }
            else if(params[0].equalsIgnoreCase("PASSWORD")){
                requestDTO.setPassword(params[1].trim());
                mandatoryParameters.setPassword(true);
            }
            else{
                throw new InvalidParameterException(params[0]);
            }
        }
        if(!mandatoryParameters.validateRegisterParameters()){
            throw new MandatoryParametersNotFoundException(mandatoryParameters.getMandatoryParametersList());
        }
        RegisterUserResponseDTO response = userController.registerUser(requestDTO);
        System.out.println("User Created with Id : " + response.getUser().getId());
    }
    public void handleUpdateProfile(String[] parseCommand) throws InvalidParameterException, MandatoryParametersNotFoundException, UserNotFoundException {
        UpdateProfileRequestDTO UpdateRequestDTO = new UpdateProfileRequestDTO();
        for(int index = 1; index < parseCommand.length; index++){
            String[] params = parseCommand[index].trim().split(" ");
            if(params[0].equalsIgnoreCase("NAME")){
                UpdateRequestDTO.setName(params[1].trim());
                mandatoryParameters.setName(true);
            }
            else if(params[0].equalsIgnoreCase("PHONE")){
                UpdateRequestDTO.setPhoneNo(params[1].trim());
                mandatoryParameters.setPhone(true);
            }
            else if(params[0].equalsIgnoreCase("PASSWORD")){
                UpdateRequestDTO.setPassword(params[1].trim());
                mandatoryParameters.setPassword(true);
            }
            else if(params[0].equalsIgnoreCase("UID")){
                UpdateRequestDTO.setId(Long.parseLong(params[1].trim()));
                mandatoryParameters.setUid(true);
            }
            else{
                throw new InvalidParameterException(params[0]);
            }
        }
        if(!mandatoryParameters.validateUpdateProfileParameters()){
            throw new MandatoryParametersNotFoundException(mandatoryParameters.getMandatoryParametersList());
        }
        UpdateProfileResponseDTO updateProfileResponseDTO = userController.updateUser(UpdateRequestDTO);
        System.out.println("User Details Updated");

    }
    public void handleAddGroup(String[] parseCommand) throws MandatoryParametersNotFoundException, UserNotFoundException, InvalidParameterException {
        AddGroupRequestDTO addGroupRequestDTO = new AddGroupRequestDTO();
        for(int index = 1; index < parseCommand.length; index++){
            String[] params = parseCommand[index].trim().split(" ");
            if(params[0].equalsIgnoreCase("ADMIN")){
                addGroupRequestDTO.setId(Long.parseLong(params[1].trim()));
                mandatoryParameters.setAdmin(true);
            }
            else if(params[0].equalsIgnoreCase("NAME")){
                addGroupRequestDTO.setName(params[1].trim());
                mandatoryParameters.setName(true);
            }
            else{
                throw new InvalidParameterException(params[0]);
            }
        }
        if(!mandatoryParameters.validateAddGroupParameters()){
            throw new MandatoryParametersNotFoundException(mandatoryParameters.getMandatoryParametersList());
        }
        AddGroupResponseDTO addGroupResponseDTO = groupController.addGroup(addGroupRequestDTO);
        System.out.println("Group "+ addGroupResponseDTO.getGroup().getId() + " created with " +
                addGroupResponseDTO.getGroup().getAdmin().getId().toString() + " as Admin");

    }
    public void handleAddMember(String[] parseCommand) throws UserNotFoundException, GroupNotFoundException, InvalidGroupAdminException, MandatoryParametersNotFoundException, InvalidParameterException {
        AddMemberRequestDTO addMemberRequestDTO = new AddMemberRequestDTO();
        for(int index = 1; index < parseCommand.length; index++){
            String[] params = parseCommand[index].trim().split(" ");
            if(params[0].equalsIgnoreCase("ADMIN")){
                addMemberRequestDTO.setId(Long.parseLong(params[1].trim()));
                mandatoryParameters.setAdmin(true);
            }
            else if(params[0].equalsIgnoreCase("GID")){
                addMemberRequestDTO.setGroupId(Long.parseLong(params[1].trim()));
                mandatoryParameters.setGid(true);
            }
            else if(params[0].equalsIgnoreCase("UID")){
                addMemberRequestDTO.setMemberId(Long.parseLong(params[1].trim()));
                mandatoryParameters.setUid(true);
            }
            else{
                throw new InvalidParameterException(params[0]);
            }
        }
        if(!mandatoryParameters.validateAddMemberParameters()){
            throw new MandatoryParametersNotFoundException(mandatoryParameters.getMandatoryParametersList());
        }
        AddMemberResponseDTO addMemberResponseDTO = groupController.addMember(addMemberRequestDTO);
        System.out.println("Member " + addMemberRequestDTO.getMemberId() + " added to the Group " +
                addMemberResponseDTO.getGroup().getId());

    }
    public void handleSettleUpGroup(String[] parseCommand) throws MandatoryParametersNotFoundException, InvalidParameterException, UserNotFoundException, GroupNotFoundException {
        Long userId = -1L;
        Long groupId = -1L;
        for(int index = 1; index < parseCommand.length; index++){
            String[] params = parseCommand[index].trim().split(" ");
            if(params[0].equalsIgnoreCase("UID")){
                userId = Long.parseLong(params[1].trim());
                mandatoryParameters.setUid(true);
            }
            else if(params[0].equalsIgnoreCase("GID")){
                groupId = Long.parseLong(params[1].trim());
                mandatoryParameters.setGid(true);
            }
            else{
                throw new InvalidParameterException(params[0]);
            }
        }
        if(!mandatoryParameters.validateSettleUpGroupParameters()){
            throw new MandatoryParametersNotFoundException(mandatoryParameters.getMandatoryParametersList());
        }
        SettleUpResponseDTO settleUpGroupResponse = groupController.settleUpGroupExpense(userId, groupId);
        System.out.format("%-25s%-25s%-20s\n","From-User","To-User","Amount");
        for(Transaction transaction : settleUpGroupResponse.getTransactionList()){
            System.out.format("%-25s%-25s%-20s\n",transaction.getFromUser().getName(),
                    transaction.getToUser().getName(),
                    transaction.getAmount().toString());
        }
    }
    public void handleSettleUp(String[] parseCommand) throws UserNotFoundException, MandatoryParametersNotFoundException, InvalidParameterException {
        Long userId = -1L;
        for(int index = 1; index < parseCommand.length; index++){
            String[] params = parseCommand[index].trim().split(" ");
            if(params[0].equalsIgnoreCase("UID")){
                userId = Long.parseLong(params[1].trim());
                mandatoryParameters.setUid(true);
            }
            else{
                throw new InvalidParameterException(params[0]);
            }
        }
        if(!mandatoryParameters.validateSettleUpParameters()){
            throw new MandatoryParametersNotFoundException(mandatoryParameters.getMandatoryParametersList());
        }
        SettleUpResponseDTO settleUpResponseDTO = userController.settleUp(userId);
        System.out.format("%-25s%-25s%-20s\n","From-User","To-User","Amount");
        for(Transaction transaction : settleUpResponseDTO.getTransactionList()){
            System.out.format("%-25s%-25s%-20s\n",transaction.getFromUser().getName(),
                    transaction.getToUser().getName(),
                    transaction.getAmount().toString());
        }
    }
    public void handleAddGroupExpense(String[] parseCommand) throws UserNotFoundException, InvalidParameterException, GroupNotFoundException, MandatoryParametersNotFoundException {
        AddGroupExpenseRequestDTO addGroupExpenseRequestDTO = new AddGroupExpenseRequestDTO();
        List<Long> grpAmounts = new ArrayList<>();
        List<Long> grpSplits = new ArrayList<>();
        for(int index = 1; index < parseCommand.length; index++){
            String[] params = parseCommand[index].trim().split(" ");
            if(params[0].equalsIgnoreCase("CREATEDBY")){
                addGroupExpenseRequestDTO.setCreatedBy(Long.parseLong(params[1].trim()));
                mandatoryParameters.setCreatedBy(true);
            }
            else if(params[0].equalsIgnoreCase("DESC")){
                addGroupExpenseRequestDTO.setDescription(params[1].trim());
                mandatoryParameters.setDesc(true);
            }
            else if(params[0].equalsIgnoreCase("GID")){
                addGroupExpenseRequestDTO.setGroupId(Long.parseLong(params[1].trim()));
                mandatoryParameters.setGid(true);
            }
            else if(params[0].equalsIgnoreCase("PAIDBY")){
                if(PaidBy.IPAY == PaidBy.valueOf(params[1].trim().toUpperCase())){
                    addGroupExpenseRequestDTO.setPaidBy(PaidBy.IPAY);
                }
                else if(PaidBy.MULTIPAY == PaidBy.valueOf(params[1].trim().toUpperCase())){
                    addGroupExpenseRequestDTO.setPaidBy(PaidBy.MULTIPAY);
                }
                else{
                    throw new InvalidParameterException(params[0]+" "+params[1]);
                }
                mandatoryParameters.setPaidBy(true);
            }
            else if(params[0].equalsIgnoreCase("AMOUNTS")){
                String[] arrAmount = params[1].trim().split(",");
                for(String amount : arrAmount) {
                    grpAmounts.add(Long.parseLong(amount));
                }
                mandatoryParameters.setAmounts(true);
            }
            else if(params[0].equalsIgnoreCase("SPLIT")){
                if(ExpenseSplit.EXACT == ExpenseSplit.valueOf(params[1].trim().toUpperCase())){
                    addGroupExpenseRequestDTO.setExpenseSplit(ExpenseSplit.EXACT);
                }
                else if(ExpenseSplit.RATIO == ExpenseSplit.valueOf(params[1].trim().toUpperCase())){
                    addGroupExpenseRequestDTO.setExpenseSplit(ExpenseSplit.RATIO);
                }
                else if(ExpenseSplit.EQUAL == ExpenseSplit.valueOf(params[1].trim().toUpperCase())){
                    addGroupExpenseRequestDTO.setExpenseSplit(ExpenseSplit.EQUAL);
                }
                else if(ExpenseSplit.PERCENT == ExpenseSplit.valueOf(params[1].trim().toUpperCase())){
                    addGroupExpenseRequestDTO.setExpenseSplit(ExpenseSplit.PERCENT);
                }
                else{
                    throw new InvalidParameterException(params[0]+" "+params[1]);
                }
                mandatoryParameters.setSplit(true);
            }
            else if(params[0].equalsIgnoreCase("SPLITS")){
                String[] arrSplit = params[1].trim().split(",");
                for(String splitVal: arrSplit) {
                    grpSplits.add(Long.parseLong(splitVal));
                }
                mandatoryParameters.setSplits(true);
            }
            else{
                throw new InvalidParameterException(params[0]);
            }
        }
        if(!mandatoryParameters.validateGroupExpenseParameters(addGroupExpenseRequestDTO.getExpenseSplit())){
            throw new MandatoryParametersNotFoundException(mandatoryParameters.getMandatoryParametersList());
        }
        addGroupExpenseRequestDTO.setAmounts(grpAmounts);
        addGroupExpenseRequestDTO.setSplit(grpSplits);

        AddGroupExpenseResponseDTO responseDTO = groupController.addGroupExpense(addGroupExpenseRequestDTO);
        int id = responseDTO.getGroup().getExpenses().size();
        System.out.println("Expense " + responseDTO.getGroup().getExpenses().get(id - 1) + " Created");

    }
    public void handleAddExpense(String[] parseCommand) throws UserNotFoundException, InvalidParameterException, MandatoryParametersNotFoundException {
        AddExpenseRequestDTO addExpenseRequestDTO = new AddExpenseRequestDTO();
        List<Long> participants = new ArrayList<>();
        List<Long> amounts = new ArrayList<>();
        List<Long> splits = new ArrayList<>();
        for(int index = 1; index < parseCommand.length; index++){
            String[] params = parseCommand[index].trim().split(" ");
            if(params[0].equalsIgnoreCase("CREATEDBY")){
                addExpenseRequestDTO.setCreatedBy(Long.parseLong(params[1].trim()));
                participants.add(Long.parseLong(params[1].trim()));
                mandatoryParameters.setCreatedBy(true);
            }
            else if(params[0].equalsIgnoreCase("DESC")){
                addExpenseRequestDTO.setDescription(params[1].trim());
                mandatoryParameters.setDesc(true);
            }
            else if(params[0].equalsIgnoreCase("UID")){
                String[] users = params[1].trim().split(",");
                for(String userId: users){
                    participants.add(Long.parseLong(userId));
                }
                mandatoryParameters.setUid(true);
            }
            else if(params[0].equalsIgnoreCase("PAIDBY")){
                if(PaidBy.IPAY == PaidBy.valueOf(params[1].trim().toUpperCase())){
                    addExpenseRequestDTO.setPaidBy(PaidBy.IPAY);
                }
                else if(PaidBy.MULTIPAY == PaidBy.valueOf(params[1].trim().toUpperCase())){
                    addExpenseRequestDTO.setPaidBy(PaidBy.MULTIPAY);
                }
                else{
                    throw new InvalidParameterException(params[0]+" "+params[1]);
                }
                mandatoryParameters.setPaidBy(true);
            }
            else if(params[0].equalsIgnoreCase("AMOUNTS")){
                String[] arrAmount = params[1].trim().split(",");
                for(String amount: arrAmount) {
                    amounts.add(Long.parseLong(amount));
                }
                mandatoryParameters.setAmounts(true);
            }
            else if(params[0].equalsIgnoreCase("SPLIT")){
                if(ExpenseSplit.EXACT == ExpenseSplit.valueOf(params[1].trim().toUpperCase())){
                    addExpenseRequestDTO.setExpenseSplit(ExpenseSplit.EXACT);
                }
                else if(ExpenseSplit.RATIO == ExpenseSplit.valueOf(params[1].trim().toUpperCase())){
                    addExpenseRequestDTO.setExpenseSplit(ExpenseSplit.RATIO);
                }
                else if(ExpenseSplit.EQUAL == ExpenseSplit.valueOf(params[1].trim().toUpperCase())){
                    addExpenseRequestDTO.setExpenseSplit(ExpenseSplit.EQUAL);
                }
                else if(ExpenseSplit.PERCENT == ExpenseSplit.valueOf(params[1].trim().toUpperCase())){
                    addExpenseRequestDTO.setExpenseSplit(ExpenseSplit.PERCENT);
                }
                else{
                    throw new InvalidParameterException(params[0]+" "+params[1]);
                }
                mandatoryParameters.setSplit(true);
            }
            else if(params[0].equalsIgnoreCase("SPLITS")){
                String[] arrSplit = params[1].trim().split(",");
                for(String splitVal: arrSplit) {
                    splits.add(Long.parseLong(splitVal));
                }
                mandatoryParameters.setSplits(true);
            }
            else{
                throw new InvalidParameterException(params[0]);
            }
        }
        if(!mandatoryParameters.validateExpenseParameters(addExpenseRequestDTO.getExpenseSplit())){
            throw new MandatoryParametersNotFoundException(mandatoryParameters.getMandatoryParametersList());
        }
        addExpenseRequestDTO.setParticipants(participants);
        addExpenseRequestDTO.setAmounts(amounts);
        addExpenseRequestDTO.setSplit(splits);
        AddExpenseResponseDTO addExpenseResponseDTO = userController.addExpense(addExpenseRequestDTO);
        System.out.println("Expense " + addExpenseResponseDTO.getExpense().getId() + " Created");

    }
    public void handleUserGroups(String[] parseCommand) throws UserNotFoundException, MandatoryParametersNotFoundException, InvalidParameterException {
        UserGroupsRequestDTO userGroupsRequestDTO = new UserGroupsRequestDTO();
        for(int index = 1; index < parseCommand.length; index++){
            String[] params = parseCommand[index].trim().split(" ");
            if(params[0].equalsIgnoreCase("UID")){
                userGroupsRequestDTO.setId(Long.parseLong(params[1].trim()));
                mandatoryParameters.setUid(true);
            }
            else{
                throw new InvalidParameterException(params[0]);
            }
        }
        if(!mandatoryParameters.validateGroupsParameters()){
            throw new MandatoryParametersNotFoundException(mandatoryParameters.getMandatoryParametersList());
        }
        UserGroupsResponseDTO userGroupsResponseDTO = userController.getGroupsById(userGroupsRequestDTO);
        System.out.println("Total Number of Groups User is Member of : " + userGroupsResponseDTO.getGroups().size());
        System.out.println("Groups: ");
        for(Group group : userGroupsResponseDTO.getGroups()){
            System.out.println(group.getName());
        }
    }
    public void handleUserHistory(String[] parseCommand) throws UserNotFoundException, MandatoryParametersNotFoundException, InvalidParameterException {
        UserExpenseHistoryRequestDTO userExpenseHistoryRequestDTO = new UserExpenseHistoryRequestDTO();
        for(int index = 1; index < parseCommand.length; index++){
            String[] params = parseCommand[index].trim().split(" ");
            if(params[0].equalsIgnoreCase("UID")){
                userExpenseHistoryRequestDTO.setId(Long.parseLong(params[1].trim()));
                mandatoryParameters.setUid(true);
            }
            else{
                throw new InvalidParameterException(params[0]);
            }
        }
        if(!mandatoryParameters.validateHistoryParameters()){
            throw new MandatoryParametersNotFoundException(mandatoryParameters.getMandatoryParametersList());
        }
        UserExpenseHistoryResponseDTO userExpenseHistoryResponseDTO = userController.getExpensesById(userExpenseHistoryRequestDTO);
        System.out.format("%-40s%-20s%-10s\n","Description","Amount","Payment Status");
        for(UserExpenseHistory record : userExpenseHistoryResponseDTO.getUserExpenseHistories()){
            System.out.format("%-40s%-20s%-10s\n",record.getDescription(),record.getAmount().toString(),record.getPaymentStatus());
        }
    }
    public void handleUserTotal(String[] parseCommand) throws UserNotFoundException, MandatoryParametersNotFoundException, InvalidParameterException {
        MyTotalRequestDTO myTotalRequestDTO = new MyTotalRequestDTO();
        for(int index = 1; index < parseCommand.length; index++){
            String[] params = parseCommand[index].trim().split(" ");
            if(params[0].equalsIgnoreCase("UID")){
                myTotalRequestDTO.setId(Long.parseLong(params[1].trim()));
                mandatoryParameters.setUid(true);
            }
            else{
                throw new InvalidParameterException(params[0]);
            }
        }
        if(!mandatoryParameters.validateMyTotalParameters()){
            throw new MandatoryParametersNotFoundException(mandatoryParameters.getMandatoryParametersList());
        }
        MyTotalResponseDTO myTotalResponseDTO = userController.getMyTotal(myTotalRequestDTO);
        System.out.println("My Total(Owed) = " + myTotalResponseDTO.getAmount().toString());

    }
}
