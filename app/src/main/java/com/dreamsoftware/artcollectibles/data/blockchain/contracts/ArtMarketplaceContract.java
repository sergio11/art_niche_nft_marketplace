package com.dreamsoftware.artcollectibles.data.blockchain.contracts;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.DynamicStruct;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.StaticStruct;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.BaseEventResponse;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 1.4.2.
 */
@SuppressWarnings("rawtypes")
public class ArtMarketplaceContract extends Contract {
    public static final String BINARY = "Bin file was not provided";

    public static final String FUNC_DEFAULT_COST_OF_PUTTING_FOR_SALE = "DEFAULT_COST_OF_PUTTING_FOR_SALE";

    public static final String FUNC_BUYITEM = "buyItem";

    public static final String FUNC_COSTOFPUTTINGFORSALE = "costOfPuttingForSale";

    public static final String FUNC_COUNTAVAILABLEMARKETITEMS = "countAvailableMarketItems";

    public static final String FUNC_COUNTCANCELEDMARKETITEMS = "countCanceledMarketItems";

    public static final String FUNC_COUNTSOLDMARKETITEMS = "countSoldMarketItems";

    public static final String FUNC_COUNTTOKENBOUGHTBYADDRESS = "countTokenBoughtByAddress";

    public static final String FUNC_COUNTTOKENSOLDBYADDRESS = "countTokenSoldByAddress";

    public static final String FUNC_COUNTTOKENTRANSACTIONS = "countTokenTransactions";

    public static final String FUNC_COUNTTOKENWITHDRAWNBYADDRESS = "countTokenWithdrawnByAddress";

    public static final String FUNC_FETCHAVAILABLEMARKETITEMS = "fetchAvailableMarketItems";

    public static final String FUNC_FETCHCREATEDMARKETITEMS = "fetchCreatedMarketItems";

    public static final String FUNC_FETCHCURRENTITEMPRICE = "fetchCurrentItemPrice";

    public static final String FUNC_FETCHITEMFORSALE = "fetchItemForSale";

    public static final String FUNC_FETCHITEMFORSALEBYMETADATACID = "fetchItemForSaleByMetadataCID";

    public static final String FUNC_FETCHLASTMARKETHISTORYITEMS = "fetchLastMarketHistoryItems";

    public static final String FUNC_FETCHMARKETHISTORY = "fetchMarketHistory";

    public static final String FUNC_FETCHMARKETHISTORYITEM = "fetchMarketHistoryItem";

    public static final String FUNC_FETCHMARKETSTATISTICS = "fetchMarketStatistics";

    public static final String FUNC_FETCHOWNEDMARKETITEMS = "fetchOwnedMarketItems";

    public static final String FUNC_FETCHPAGINATEDAVAILABLEMARKETITEMS = "fetchPaginatedAvailableMarketItems";

    public static final String FUNC_FETCHPAGINATEDCREATEDMARKETITEMS = "fetchPaginatedCreatedMarketItems";

    public static final String FUNC_FETCHPAGINATEDOWNEDMARKETITEMS = "fetchPaginatedOwnedMarketItems";

    public static final String FUNC_FETCHPAGINATEDSELLINGMARKETITEMS = "fetchPaginatedSellingMarketItems";

    public static final String FUNC_FETCHPAGINATEDTOKENMARKETHISTORY = "fetchPaginatedTokenMarketHistory";

    public static final String FUNC_FETCHSELLINGMARKETITEMS = "fetchSellingMarketItems";

    public static final String FUNC_FETCHTOKENMARKETHISTORY = "fetchTokenMarketHistory";

    public static final String FUNC_FETCHTOKENMARKETHISTORYPRICES = "fetchTokenMarketHistoryPrices";

    public static final String FUNC_FETCHWALLETSTATISTICS = "fetchWalletStatistics";

    public static final String FUNC_GETARTCOLLECTIBLEADDRESS = "getArtCollectibleAddress";

    public static final String FUNC_ISTOKENADDEDFORSALE = "isTokenAddedForSale";

    public static final String FUNC_ISTOKENMETADATACIDADDEDFORSALE = "isTokenMetadataCIDAddedForSale";

    public static final String FUNC_OWNER = "owner";

    public static final String FUNC_PUTITEMFORSALE = "putItemForSale";

    public static final String FUNC_RENOUNCEOWNERSHIP = "renounceOwnership";

    public static final String FUNC_SETARTCOLLECTIBLEADDRESS = "setArtCollectibleAddress";

    public static final String FUNC_SETCOSTOFPUTTINGFORSALE = "setCostOfPuttingForSale";

    public static final String FUNC_TRANSFEROWNERSHIP = "transferOwnership";

    public static final String FUNC_WITHDRAWFROMSALE = "withdrawFromSale";

    public static final Event ARTCOLLECTIBLEADDEDFORSALE_EVENT = new Event("ArtCollectibleAddedForSale", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event ARTCOLLECTIBLESOLD_EVENT = new Event("ArtCollectibleSold", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Address>() {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event ARTCOLLECTIBLEWITHDRAWNFROMSALE_EVENT = new Event("ArtCollectibleWithdrawnFromSale", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
    ;

    public static final Event OWNERSHIPTRANSFERRED_EVENT = new Event("OwnershipTransferred", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}));
    ;

    @Deprecated
    protected ArtMarketplaceContract(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected ArtMarketplaceContract(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected ArtMarketplaceContract(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected ArtMarketplaceContract(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static List<ArtCollectibleAddedForSaleEventResponse> getArtCollectibleAddedForSaleEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = staticExtractEventParametersWithLog(ARTCOLLECTIBLEADDEDFORSALE_EVENT, transactionReceipt);
        ArrayList<ArtCollectibleAddedForSaleEventResponse> responses = new ArrayList<ArtCollectibleAddedForSaleEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            ArtCollectibleAddedForSaleEventResponse typedResponse = new ArtCollectibleAddedForSaleEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.id = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.tokenId = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.price = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<ArtCollectibleAddedForSaleEventResponse> artCollectibleAddedForSaleEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, ArtCollectibleAddedForSaleEventResponse>() {
            @Override
            public ArtCollectibleAddedForSaleEventResponse apply(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(ARTCOLLECTIBLEADDEDFORSALE_EVENT, log);
                ArtCollectibleAddedForSaleEventResponse typedResponse = new ArtCollectibleAddedForSaleEventResponse();
                typedResponse.log = log;
                typedResponse.id = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.tokenId = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
                typedResponse.price = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<ArtCollectibleAddedForSaleEventResponse> artCollectibleAddedForSaleEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(ARTCOLLECTIBLEADDEDFORSALE_EVENT));
        return artCollectibleAddedForSaleEventFlowable(filter);
    }

    public static List<ArtCollectibleSoldEventResponse> getArtCollectibleSoldEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = staticExtractEventParametersWithLog(ARTCOLLECTIBLESOLD_EVENT, transactionReceipt);
        ArrayList<ArtCollectibleSoldEventResponse> responses = new ArrayList<ArtCollectibleSoldEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            ArtCollectibleSoldEventResponse typedResponse = new ArtCollectibleSoldEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.tokenId = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.buyer = (String) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.price = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<ArtCollectibleSoldEventResponse> artCollectibleSoldEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, ArtCollectibleSoldEventResponse>() {
            @Override
            public ArtCollectibleSoldEventResponse apply(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(ARTCOLLECTIBLESOLD_EVENT, log);
                ArtCollectibleSoldEventResponse typedResponse = new ArtCollectibleSoldEventResponse();
                typedResponse.log = log;
                typedResponse.tokenId = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.buyer = (String) eventValues.getNonIndexedValues().get(1).getValue();
                typedResponse.price = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<ArtCollectibleSoldEventResponse> artCollectibleSoldEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(ARTCOLLECTIBLESOLD_EVENT));
        return artCollectibleSoldEventFlowable(filter);
    }

    public static List<ArtCollectibleWithdrawnFromSaleEventResponse> getArtCollectibleWithdrawnFromSaleEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = staticExtractEventParametersWithLog(ARTCOLLECTIBLEWITHDRAWNFROMSALE_EVENT, transactionReceipt);
        ArrayList<ArtCollectibleWithdrawnFromSaleEventResponse> responses = new ArrayList<ArtCollectibleWithdrawnFromSaleEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            ArtCollectibleWithdrawnFromSaleEventResponse typedResponse = new ArtCollectibleWithdrawnFromSaleEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.tokenId = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<ArtCollectibleWithdrawnFromSaleEventResponse> artCollectibleWithdrawnFromSaleEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, ArtCollectibleWithdrawnFromSaleEventResponse>() {
            @Override
            public ArtCollectibleWithdrawnFromSaleEventResponse apply(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(ARTCOLLECTIBLEWITHDRAWNFROMSALE_EVENT, log);
                ArtCollectibleWithdrawnFromSaleEventResponse typedResponse = new ArtCollectibleWithdrawnFromSaleEventResponse();
                typedResponse.log = log;
                typedResponse.tokenId = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<ArtCollectibleWithdrawnFromSaleEventResponse> artCollectibleWithdrawnFromSaleEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(ARTCOLLECTIBLEWITHDRAWNFROMSALE_EVENT));
        return artCollectibleWithdrawnFromSaleEventFlowable(filter);
    }

    public static List<OwnershipTransferredEventResponse> getOwnershipTransferredEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = staticExtractEventParametersWithLog(OWNERSHIPTRANSFERRED_EVENT, transactionReceipt);
        ArrayList<OwnershipTransferredEventResponse> responses = new ArrayList<OwnershipTransferredEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            OwnershipTransferredEventResponse typedResponse = new OwnershipTransferredEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.previousOwner = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.newOwner = (String) eventValues.getIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<OwnershipTransferredEventResponse> ownershipTransferredEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, OwnershipTransferredEventResponse>() {
            @Override
            public OwnershipTransferredEventResponse apply(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(OWNERSHIPTRANSFERRED_EVENT, log);
                OwnershipTransferredEventResponse typedResponse = new OwnershipTransferredEventResponse();
                typedResponse.log = log;
                typedResponse.previousOwner = (String) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.newOwner = (String) eventValues.getIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<OwnershipTransferredEventResponse> ownershipTransferredEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(OWNERSHIPTRANSFERRED_EVENT));
        return ownershipTransferredEventFlowable(filter);
    }

    public RemoteFunctionCall<BigInteger> DEFAULT_COST_OF_PUTTING_FOR_SALE() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_DEFAULT_COST_OF_PUTTING_FOR_SALE, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<TransactionReceipt> buyItem(BigInteger tokenId, BigInteger weiValue) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_BUYITEM, 
                Arrays.<Type>asList(new Uint256(tokenId)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function, weiValue);
    }

    public RemoteFunctionCall<BigInteger> costOfPuttingForSale() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_COSTOFPUTTINGFORSALE, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<BigInteger> countAvailableMarketItems() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_COUNTAVAILABLEMARKETITEMS, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<BigInteger> countCanceledMarketItems() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_COUNTCANCELEDMARKETITEMS, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<BigInteger> countSoldMarketItems() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_COUNTSOLDMARKETITEMS, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<BigInteger> countTokenBoughtByAddress(String ownerAddress) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_COUNTTOKENBOUGHTBYADDRESS, 
                Arrays.<Type>asList(new Address(160, ownerAddress)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<BigInteger> countTokenSoldByAddress(String ownerAddress) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_COUNTTOKENSOLDBYADDRESS, 
                Arrays.<Type>asList(new Address(160, ownerAddress)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<BigInteger> countTokenTransactions(BigInteger tokenId) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_COUNTTOKENTRANSACTIONS, 
                Arrays.<Type>asList(new Uint256(tokenId)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<BigInteger> countTokenWithdrawnByAddress(String ownerAddress) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_COUNTTOKENWITHDRAWNBYADDRESS, 
                Arrays.<Type>asList(new Address(160, ownerAddress)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<List> fetchAvailableMarketItems() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_FETCHAVAILABLEMARKETITEMS, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicArray<ArtCollectibleForSale>>() {}));
        return new RemoteFunctionCall<List>(function,
                new Callable<List>() {
                    @Override
                    @SuppressWarnings("unchecked")
                    public List call() throws Exception {
                        List<Type> result = (List<Type>) executeCallSingleValueReturn(function, List.class);
                        return convertToNative(result);
                    }
                });
    }

    public RemoteFunctionCall<List> fetchCreatedMarketItems() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_FETCHCREATEDMARKETITEMS, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicArray<ArtCollectibleForSale>>() {}));
        return new RemoteFunctionCall<List>(function,
                new Callable<List>() {
                    @Override
                    @SuppressWarnings("unchecked")
                    public List call() throws Exception {
                        List<Type> result = (List<Type>) executeCallSingleValueReturn(function, List.class);
                        return convertToNative(result);
                    }
                });
    }

    public RemoteFunctionCall<BigInteger> fetchCurrentItemPrice(BigInteger tokenId) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_FETCHCURRENTITEMPRICE, 
                Arrays.<Type>asList(new Uint256(tokenId)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<ArtCollectibleForSale> fetchItemForSale(BigInteger tokenId) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_FETCHITEMFORSALE, 
                Arrays.<Type>asList(new Uint256(tokenId)),
                Arrays.<TypeReference<?>>asList(new TypeReference<ArtCollectibleForSale>() {}));
        return executeRemoteCallSingleValueReturn(function, ArtCollectibleForSale.class);
    }

    public RemoteFunctionCall<ArtCollectibleForSale> fetchItemForSaleByMetadataCID(String metadataCID) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_FETCHITEMFORSALEBYMETADATACID, 
                Arrays.<Type>asList(new Utf8String(metadataCID)),
                Arrays.<TypeReference<?>>asList(new TypeReference<ArtCollectibleForSale>() {}));
        return executeRemoteCallSingleValueReturn(function, ArtCollectibleForSale.class);
    }

    public RemoteFunctionCall<List> fetchLastMarketHistoryItems(BigInteger count) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_FETCHLASTMARKETHISTORYITEMS, 
                Arrays.<Type>asList(new Uint256(count)),
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicArray<ArtCollectibleForSale>>() {}));
        return new RemoteFunctionCall<List>(function,
                new Callable<List>() {
                    @Override
                    @SuppressWarnings("unchecked")
                    public List call() throws Exception {
                        List<Type> result = (List<Type>) executeCallSingleValueReturn(function, List.class);
                        return convertToNative(result);
                    }
                });
    }

    public RemoteFunctionCall<List> fetchMarketHistory() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_FETCHMARKETHISTORY, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicArray<ArtCollectibleForSale>>() {}));
        return new RemoteFunctionCall<List>(function,
                new Callable<List>() {
                    @Override
                    @SuppressWarnings("unchecked")
                    public List call() throws Exception {
                        List<Type> result = (List<Type>) executeCallSingleValueReturn(function, List.class);
                        return convertToNative(result);
                    }
                });
    }

    public RemoteFunctionCall<ArtCollectibleForSale> fetchMarketHistoryItem(BigInteger marketItemId) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_FETCHMARKETHISTORYITEM, 
                Arrays.<Type>asList(new Uint256(marketItemId)),
                Arrays.<TypeReference<?>>asList(new TypeReference<ArtCollectibleForSale>() {}));
        return executeRemoteCallSingleValueReturn(function, ArtCollectibleForSale.class);
    }

    public RemoteFunctionCall<MarketStatistics> fetchMarketStatistics() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_FETCHMARKETSTATISTICS, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<MarketStatistics>() {}));
        return executeRemoteCallSingleValueReturn(function, MarketStatistics.class);
    }

    public RemoteFunctionCall<List> fetchOwnedMarketItems() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_FETCHOWNEDMARKETITEMS, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicArray<ArtCollectibleForSale>>() {}));
        return new RemoteFunctionCall<List>(function,
                new Callable<List>() {
                    @Override
                    @SuppressWarnings("unchecked")
                    public List call() throws Exception {
                        List<Type> result = (List<Type>) executeCallSingleValueReturn(function, List.class);
                        return convertToNative(result);
                    }
                });
    }

    public RemoteFunctionCall<List> fetchPaginatedAvailableMarketItems(BigInteger count) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_FETCHPAGINATEDAVAILABLEMARKETITEMS, 
                Arrays.<Type>asList(new Uint256(count)),
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicArray<ArtCollectibleForSale>>() {}));
        return new RemoteFunctionCall<List>(function,
                new Callable<List>() {
                    @Override
                    @SuppressWarnings("unchecked")
                    public List call() throws Exception {
                        List<Type> result = (List<Type>) executeCallSingleValueReturn(function, List.class);
                        return convertToNative(result);
                    }
                });
    }

    public RemoteFunctionCall<List> fetchPaginatedCreatedMarketItems(BigInteger count) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_FETCHPAGINATEDCREATEDMARKETITEMS, 
                Arrays.<Type>asList(new Uint256(count)),
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicArray<ArtCollectibleForSale>>() {}));
        return new RemoteFunctionCall<List>(function,
                new Callable<List>() {
                    @Override
                    @SuppressWarnings("unchecked")
                    public List call() throws Exception {
                        List<Type> result = (List<Type>) executeCallSingleValueReturn(function, List.class);
                        return convertToNative(result);
                    }
                });
    }

    public RemoteFunctionCall<List> fetchPaginatedOwnedMarketItems(BigInteger count) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_FETCHPAGINATEDOWNEDMARKETITEMS, 
                Arrays.<Type>asList(new Uint256(count)),
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicArray<ArtCollectibleForSale>>() {}));
        return new RemoteFunctionCall<List>(function,
                new Callable<List>() {
                    @Override
                    @SuppressWarnings("unchecked")
                    public List call() throws Exception {
                        List<Type> result = (List<Type>) executeCallSingleValueReturn(function, List.class);
                        return convertToNative(result);
                    }
                });
    }

    public RemoteFunctionCall<List> fetchPaginatedSellingMarketItems(BigInteger count) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_FETCHPAGINATEDSELLINGMARKETITEMS, 
                Arrays.<Type>asList(new Uint256(count)),
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicArray<ArtCollectibleForSale>>() {}));
        return new RemoteFunctionCall<List>(function,
                new Callable<List>() {
                    @Override
                    @SuppressWarnings("unchecked")
                    public List call() throws Exception {
                        List<Type> result = (List<Type>) executeCallSingleValueReturn(function, List.class);
                        return convertToNative(result);
                    }
                });
    }

    public RemoteFunctionCall<List> fetchPaginatedTokenMarketHistory(BigInteger tokenId, BigInteger count) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_FETCHPAGINATEDTOKENMARKETHISTORY, 
                Arrays.<Type>asList(new Uint256(tokenId),
                new Uint256(count)),
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicArray<ArtCollectibleForSale>>() {}));
        return new RemoteFunctionCall<List>(function,
                new Callable<List>() {
                    @Override
                    @SuppressWarnings("unchecked")
                    public List call() throws Exception {
                        List<Type> result = (List<Type>) executeCallSingleValueReturn(function, List.class);
                        return convertToNative(result);
                    }
                });
    }

    public RemoteFunctionCall<List> fetchSellingMarketItems() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_FETCHSELLINGMARKETITEMS, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicArray<ArtCollectibleForSale>>() {}));
        return new RemoteFunctionCall<List>(function,
                new Callable<List>() {
                    @Override
                    @SuppressWarnings("unchecked")
                    public List call() throws Exception {
                        List<Type> result = (List<Type>) executeCallSingleValueReturn(function, List.class);
                        return convertToNative(result);
                    }
                });
    }

    public RemoteFunctionCall<List> fetchTokenMarketHistory(BigInteger tokenId) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_FETCHTOKENMARKETHISTORY, 
                Arrays.<Type>asList(new Uint256(tokenId)),
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicArray<ArtCollectibleForSale>>() {}));
        return new RemoteFunctionCall<List>(function,
                new Callable<List>() {
                    @Override
                    @SuppressWarnings("unchecked")
                    public List call() throws Exception {
                        List<Type> result = (List<Type>) executeCallSingleValueReturn(function, List.class);
                        return convertToNative(result);
                    }
                });
    }

    public RemoteFunctionCall<List> fetchTokenMarketHistoryPrices(BigInteger tokenId) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_FETCHTOKENMARKETHISTORYPRICES, 
                Arrays.<Type>asList(new Uint256(tokenId)),
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicArray<ArtCollectibleMarketPrice>>() {}));
        return new RemoteFunctionCall<List>(function,
                new Callable<List>() {
                    @Override
                    @SuppressWarnings("unchecked")
                    public List call() throws Exception {
                        List<Type> result = (List<Type>) executeCallSingleValueReturn(function, List.class);
                        return convertToNative(result);
                    }
                });
    }

    public RemoteFunctionCall<WalletStatistics> fetchWalletStatistics(String ownerAddress) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_FETCHWALLETSTATISTICS, 
                Arrays.<Type>asList(new Address(160, ownerAddress)),
                Arrays.<TypeReference<?>>asList(new TypeReference<WalletStatistics>() {}));
        return executeRemoteCallSingleValueReturn(function, WalletStatistics.class);
    }

    public RemoteFunctionCall<String> getArtCollectibleAddress() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_GETARTCOLLECTIBLEADDRESS, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<Boolean> isTokenAddedForSale(BigInteger tokenId) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_ISTOKENADDEDFORSALE, 
                Arrays.<Type>asList(new Uint256(tokenId)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteFunctionCall<Boolean> isTokenMetadataCIDAddedForSale(String metadataCID) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_ISTOKENMETADATACIDADDEDFORSALE, 
                Arrays.<Type>asList(new Utf8String(metadataCID)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteFunctionCall<String> owner() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_OWNER, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<TransactionReceipt> putItemForSale(BigInteger tokenId, BigInteger price, BigInteger weiValue) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_PUTITEMFORSALE, 
                Arrays.<Type>asList(new Uint256(tokenId),
                new Uint256(price)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function, weiValue);
    }

    public RemoteFunctionCall<TransactionReceipt> renounceOwnership() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_RENOUNCEOWNERSHIP, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> setArtCollectibleAddress(String artCollectibleAddress, BigInteger weiValue) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_SETARTCOLLECTIBLEADDRESS, 
                Arrays.<Type>asList(new Address(160, artCollectibleAddress)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function, weiValue);
    }

    public RemoteFunctionCall<TransactionReceipt> setCostOfPuttingForSale(BigInteger _costOfPuttingForSale) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_SETCOSTOFPUTTINGFORSALE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint8(_costOfPuttingForSale)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> transferOwnership(String newOwner) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_TRANSFEROWNERSHIP, 
                Arrays.<Type>asList(new Address(160, newOwner)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> withdrawFromSale(BigInteger tokenId) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_WITHDRAWFROMSALE, 
                Arrays.<Type>asList(new Uint256(tokenId)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    @Deprecated
    public static ArtMarketplaceContract load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new ArtMarketplaceContract(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static ArtMarketplaceContract load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new ArtMarketplaceContract(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static ArtMarketplaceContract load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new ArtMarketplaceContract(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static ArtMarketplaceContract load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new ArtMarketplaceContract(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static class ArtCollectibleForSale extends DynamicStruct {
        public BigInteger marketItemId;

        public BigInteger tokenId;

        public String metadataCID;

        public String creator;

        public String seller;

        public String owner;

        public BigInteger price;

        public BigInteger putForSaleAt;

        public Boolean sold;

        public BigInteger soldAt;

        public Boolean canceled;

        public BigInteger canceledAt;

        public ArtCollectibleForSale(BigInteger marketItemId, BigInteger tokenId, String metadataCID, String creator, String seller, String owner, BigInteger price, BigInteger putForSaleAt, Boolean sold, BigInteger soldAt, Boolean canceled, BigInteger canceledAt) {
            super(new Uint256(marketItemId),
                    new Uint256(tokenId),
                    new Utf8String(metadataCID),
                    new Address(160, creator),
                    new Address(160, seller),
                    new Address(160, owner),
                    new Uint256(price),
                    new Uint256(putForSaleAt),
                    new Bool(sold),
                    new Uint256(soldAt),
                    new Bool(canceled),
                    new Uint256(canceledAt));
            this.marketItemId = marketItemId;
            this.tokenId = tokenId;
            this.metadataCID = metadataCID;
            this.creator = creator;
            this.seller = seller;
            this.owner = owner;
            this.price = price;
            this.putForSaleAt = putForSaleAt;
            this.sold = sold;
            this.soldAt = soldAt;
            this.canceled = canceled;
            this.canceledAt = canceledAt;
        }

        public ArtCollectibleForSale(Uint256 marketItemId, Uint256 tokenId, Utf8String metadataCID, Address creator, Address seller, Address owner, Uint256 price, Uint256 putForSaleAt, Bool sold, Uint256 soldAt, Bool canceled, Uint256 canceledAt) {
            super(marketItemId, tokenId, metadataCID, creator, seller, owner, price, putForSaleAt, sold, soldAt, canceled, canceledAt);
            this.marketItemId = marketItemId.getValue();
            this.tokenId = tokenId.getValue();
            this.metadataCID = metadataCID.getValue();
            this.creator = creator.getValue();
            this.seller = seller.getValue();
            this.owner = owner.getValue();
            this.price = price.getValue();
            this.putForSaleAt = putForSaleAt.getValue();
            this.sold = sold.getValue();
            this.soldAt = soldAt.getValue();
            this.canceled = canceled.getValue();
            this.canceledAt = canceledAt.getValue();
        }
    }

    public static class MarketStatistics extends StaticStruct {
        public BigInteger countAvailable;

        public BigInteger countSold;

        public BigInteger countCanceled;

        public MarketStatistics(BigInteger countAvailable, BigInteger countSold, BigInteger countCanceled) {
            super(new Uint256(countAvailable),
                    new Uint256(countSold),
                    new Uint256(countCanceled));
            this.countAvailable = countAvailable;
            this.countSold = countSold;
            this.countCanceled = countCanceled;
        }

        public MarketStatistics(Uint256 countAvailable, Uint256 countSold, Uint256 countCanceled) {
            super(countAvailable, countSold, countCanceled);
            this.countAvailable = countAvailable.getValue();
            this.countSold = countSold.getValue();
            this.countCanceled = countCanceled.getValue();
        }
    }

    public static class ArtCollectibleMarketPrice extends StaticStruct {
        public BigInteger marketItemId;

        public BigInteger tokenId;

        public BigInteger price;

        public BigInteger date;

        public ArtCollectibleMarketPrice(BigInteger marketItemId, BigInteger tokenId, BigInteger price, BigInteger date) {
            super(new Uint256(marketItemId),
                    new Uint256(tokenId),
                    new Uint256(price),
                    new Uint256(date));
            this.marketItemId = marketItemId;
            this.tokenId = tokenId;
            this.price = price;
            this.date = date;
        }

        public ArtCollectibleMarketPrice(Uint256 marketItemId, Uint256 tokenId, Uint256 price, Uint256 date) {
            super(marketItemId, tokenId, price, date);
            this.marketItemId = marketItemId.getValue();
            this.tokenId = tokenId.getValue();
            this.price = price.getValue();
            this.date = date.getValue();
        }
    }

    public static class WalletStatistics extends StaticStruct {
        public BigInteger countTokenSold;

        public BigInteger countTokenBought;

        public BigInteger countTokenWithdrawn;

        public WalletStatistics(BigInteger countTokenSold, BigInteger countTokenBought, BigInteger countTokenWithdrawn) {
            super(new Uint256(countTokenSold),
                    new Uint256(countTokenBought),
                    new Uint256(countTokenWithdrawn));
            this.countTokenSold = countTokenSold;
            this.countTokenBought = countTokenBought;
            this.countTokenWithdrawn = countTokenWithdrawn;
        }

        public WalletStatistics(Uint256 countTokenSold, Uint256 countTokenBought, Uint256 countTokenWithdrawn) {
            super(countTokenSold, countTokenBought, countTokenWithdrawn);
            this.countTokenSold = countTokenSold.getValue();
            this.countTokenBought = countTokenBought.getValue();
            this.countTokenWithdrawn = countTokenWithdrawn.getValue();
        }
    }

    public static class ArtCollectibleAddedForSaleEventResponse extends BaseEventResponse {
        public BigInteger id;

        public BigInteger tokenId;

        public BigInteger price;
    }

    public static class ArtCollectibleSoldEventResponse extends BaseEventResponse {
        public BigInteger tokenId;

        public String buyer;

        public BigInteger price;
    }

    public static class ArtCollectibleWithdrawnFromSaleEventResponse extends BaseEventResponse {
        public BigInteger tokenId;
    }

    public static class OwnershipTransferredEventResponse extends BaseEventResponse {
        public String previousOwner;

        public String newOwner;
    }
}
