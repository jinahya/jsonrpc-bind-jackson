package com.github.jinahya.jsonrpc.bind.v2.examples.jsonrpc_org;

import com.fasterxml.jackson.databind.node.NumericNode;
import com.github.jinahya.jsonrpc.bind.v2.ResponseObject.ErrorObject;
import com.github.jinahya.jsonrpc.bind.v2.jackson.JacksonServerResponse;

public class NamedParametersServerResponse extends JacksonServerResponse<Integer, ErrorObject<Void>, NumericNode> {

}
