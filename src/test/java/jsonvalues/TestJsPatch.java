package jsonvalues;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 tests have been adapted from https://github.com/json-patch/json-patch-tests/blob/master/tests.json
 */
public class TestJsPatch
{
    String tests = "[\n"
    + "    { \"comment\": \"empty list, empty docs\",\n"
    + "      \"doc\": {},\n"
    + "      \"patch\": [],\n"
    + "      \"expected\": {} },\n"
    + "\n"
    + "    { \"comment\": \"empty patch list\",\n"
    + "      \"doc\": {\"foo\": 1},\n"
    + "      \"patch\": [],\n"
    + "      \"expected\": {\"foo\": 1} },\n"
    + "\n"
    + "    { \"comment\": \"rearrangements OK?\",\n"
    + "      \"doc\": {\"foo\": 1, \"bar\": 2},\n"
    + "      \"patch\": [],\n"
    + "      \"expected\": {\"bar\":2, \"foo\": 1} },\n"
    + "\n"
    + "    { \"comment\": \"rearrangements OK?  How about one level down ... array\",\n"
    + "      \"doc\": [{\"foo\": 1, \"bar\": 2}],\n"
    + "      \"patch\": [],\n"
    + "      \"expected\": [{\"bar\":2, \"foo\": 1}] },\n"
    + "\n"
    + "    { \"comment\": \"rearrangements OK?  How about one level down...\",\n"
    + "      \"doc\": {\"foo\":{\"foo\": 1, \"bar\": 2}},\n"
    + "      \"patch\": [],\n"
    + "      \"expected\": {\"foo\":{\"bar\":2, \"foo\": 1}} },\n"
    + "\n"
    + "    { \"comment\": \"add replaces any existing field\",\n"
    + "      \"doc\": {\"foo\": null},\n"
    + "      \"patch\": [{\"op\": \"add\", \"path\": \"/foo\", \"value\":1}],\n"
    + "      \"expected\": {\"foo\": 1} },\n"
    + "\n"
    + "    { \"comment\": \"toplevel array\",\n"
    + "      \"doc\": [],\n"
    + "      \"patch\": [{\"op\": \"add\", \"path\": \"/0\", \"value\": \"foo\"}],\n"
    + "      \"expected\": [\"foo\"] },\n"
    + "\n"
    + "    { \"comment\": \"toplevel array, no change\",\n"
    + "      \"doc\": [\"foo\"],\n"
    + "      \"patch\": [],\n"
    + "      \"expected\": [\"foo\"] },\n"
    + "\n"
    + "    { \"comment\": \"toplevel object, numeric string\",\n"
    + "      \"doc\": {},\n"
    + "      \"patch\": [{\"op\": \"add\", \"path\": \"/foo\", \"value\": \"1\"}],\n"
    + "      \"expected\": {\"foo\":\"1\"} },\n"
    + "\n"
    + "    { \"comment\": \"toplevel object, integer\",\n"
    + "      \"doc\": {},\n"
    + "      \"patch\": [{\"op\": \"add\", \"path\": \"/foo\", \"value\": 1}],\n"
    + "      \"expected\": {\"foo\":1} },\n"
    + "\n"
    + "    { \"comment\": \"append to root array document?\",\n"
    + "      \"doc\": [],\n"
    + "      \"patch\": [{\"op\": \"add\", \"path\": \"/-1\", \"value\": \"hi\"}],\n"
    + "      \"expected\": [\"hi\"] },\n"
    + "\n"
    + "    { \"comment\": \"Add, / target\",\n"
    + "      \"doc\": {},\n"
    + "      \"patch\": [ {\"op\": \"add\", \"path\": \"/\", \"value\":1 } ],\n"
    + "      \"expected\": {\"\":1} },\n"
    + "\n"
    + "    { \"comment\": \"Add, /foo/ deep target (trailing slash)\",\n"
    + "      \"doc\": {\"foo\": {}},\n"
    + "      \"patch\": [ {\"op\": \"add\", \"path\": \"/foo/\", \"value\":1 } ],\n"
    + "      \"expected\": {\"foo\":{\"\": 1}} },\n"
    + "\n"
    + "    { \"comment\": \"Add composite value at top level\",\n"
    + "      \"doc\": {\"foo\": 1},\n"
    + "      \"patch\": [{\"op\": \"add\", \"path\": \"/bar\", \"value\": [1, 2]}],\n"
    + "      \"expected\": {\"foo\": 1, \"bar\": [1, 2]} },\n"
    + "\n"
    + "    { \"comment\": \"Add into composite value\",\n"
    + "      \"doc\": {\"foo\": 1, \"baz\": [{\"qux\": \"hello\"}]},\n"
    + "      \"patch\": [{\"op\": \"add\", \"path\": \"/baz/0/foo\", \"value\": \"world\"}],\n"
    + "      \"expected\": {\"foo\": 1, \"baz\": [{\"qux\": \"hello\", \"foo\": \"world\"}]} },\n"
    + "\n"
    + "    { \"doc\": {\"bar\": [1, 2]},\n"
    + "      \"patch\": [{\"op\": \"add\", \"path\": \"/bar/8\", \"value\": \"5\"}],\n"
    + "      \"error\": \"Out of bounds (upper)\" },\n"
    + "\n"
    + "    { \"doc\": {\"bar\": [1, 2]},\n"
    + "      \"patch\": [{\"op\": \"add\", \"path\": \"/bar/-2\", \"value\": \"5\"}],\n"
    + "      \"error\": \"Out of bounds (lower)\" },\n"
    + "\n"
    + "    { \"doc\": {\"foo\": 1},\n"
    + "      \"patch\": [{\"op\": \"add\", \"path\": \"/bar\", \"value\": true}],\n"
    + "      \"expected\": {\"foo\": 1, \"bar\": true} },\n"
    + "\n"
    + "    { \"doc\": {\"foo\": 1},\n"
    + "      \"patch\": [{\"op\": \"add\", \"path\": \"/bar\", \"value\": false}],\n"
    + "      \"expected\": {\"foo\": 1, \"bar\": false} },\n"
    + "\n"
    + "    { \"doc\": {\"foo\": 1},\n"
    + "      \"patch\": [{\"op\": \"add\", \"path\": \"/bar\", \"value\": null}],\n"
    + "      \"expected\": {\"foo\": 1, \"bar\": null} },\n"
    + "\n"
    + "    { \"comment\": \"keys which names are numbers has to be single-quouted\",\n"
    + "      \"doc\": {\"foo\": 1},\n"
    + "      \"patch\": [{\"op\": \"add\", \"path\": \"/'0'\", \"value\": \"bar\"}],\n"
    + "      \"expected\": {\"foo\": 1, \"0\": \"bar\" } },\n"
    + "\n"
    + "    { \"doc\": [\"foo\"],\n"
    + "      \"patch\": [{\"op\": \"add\", \"path\": \"/1\", \"value\": \"bar\"}],\n"
    + "      \"expected\": [\"foo\", \"bar\"] },\n"
    + "\n"
    + "    { \"doc\": [\"foo\", \"sil\"],\n"
    + "      \"patch\": [{\"op\": \"add\", \"path\": \"/1\", \"value\": \"bar\"}],\n"
    + "      \"expected\": [\"foo\", \"bar\", \"sil\"] },\n"
    + "\n"
    + "    { \"doc\": [\"foo\", \"sil\"],\n"
    + "      \"patch\": [{\"op\": \"add\", \"path\": \"/0\", \"value\": \"bar\"}],\n"
    + "      \"expected\": [\"bar\", \"foo\", \"sil\"] },\n"
    + "\n"
    + "    { \"comment\": \"push item to array via last index + 1\",\n"
    + "      \"doc\": [\"foo\", \"sil\"],\n"
    + "      \"patch\": [{\"op\":\"add\", \"path\": \"/2\", \"value\": \"bar\"}],\n"
    + "      \"expected\": [\"foo\", \"sil\", \"bar\"] },\n"
    + "\n"
    + "    { \"comment\": \"add item to array at index > length should fail\",\n"
    + "      \"doc\": [\"foo\", \"sil\"],\n"
    + "      \"patch\": [{\"op\":\"add\", \"path\": \"/3\", \"value\": \"bar\"}],\n"
    + "      \"error\": \"index is greater than number of items in array\" },\n"
    + "      \n"
    + "    { \"comment\": \"test against implementation-specific numeric parsing\",\n"
    + "      \"doc\": {\"1e0\": \"foo\"},\n"
    + "      \"patch\": [{\"op\": \"test\", \"path\": \"/1e0\", \"value\": \"foo\"}],\n"
    + "      \"expected\": {\"1e0\": \"foo\"} },\n"
    + "\n"
    + "    { \"comment\": \"test with bad number should fail\",\n"
    + "      \"doc\": [\"foo\", \"bar\"],\n"
    + "      \"patch\": [{\"op\": \"test\", \"path\": \"/1e0\", \"value\": \"bar\"}],\n"
    + "      \"error\": \"test op shouldn't get array element 1\" },\n"
    + "\n"
    + "    { \"doc\": [\"foo\", \"sil\"],\n"
    + "      \"patch\": [{\"op\": \"add\", \"path\": \"/bar\", \"value\": 42}],\n"
    + "      \"error\": \"Object operation on array target\" },\n"
    + "\n"
    + "    { \"doc\": [\"foo\", \"sil\"],\n"
    + "      \"patch\": [{\"op\": \"add\", \"path\": \"/1\", \"value\": [\"bar\", \"baz\"]}],\n"
    + "      \"expected\": [\"foo\", [\"bar\", \"baz\"], \"sil\"],\n"
    + "      \"comment\": \"value in array add not flattened\" },\n"
    + "\n"
    + "    { \"doc\": {\"foo\": 1, \"bar\": [1, 2, 3, 4]},\n"
    + "      \"patch\": [{\"op\": \"remove\", \"path\": \"/bar\"}],\n"
    + "      \"expected\": {\"foo\": 1} },\n"
    + "\n"
    + "    { \"doc\": {\"foo\": 1, \"baz\": [{\"qux\": \"hello\"}]},\n"
    + "      \"patch\": [{\"op\": \"remove\", \"path\": \"/baz/0/qux\"}],\n"
    + "      \"expected\": {\"foo\": 1, \"baz\": [{}]} },\n"
    + "\n"
    + "    { \"doc\": {\"foo\": 1, \"baz\": [{\"qux\": \"hello\"}]},\n"
    + "      \"patch\": [{\"op\": \"replace\", \"path\": \"/foo\", \"value\": [1, 2, 3, 4]}],\n"
    + "      \"expected\": {\"foo\": [1, 2, 3, 4], \"baz\": [{\"qux\": \"hello\"}]} },\n"
    + "\n"
    + "    { \"doc\": {\"foo\": [1, 2, 3, 4], \"baz\": [{\"qux\": \"hello\"}]},\n"
    + "      \"patch\": [{\"op\": \"replace\", \"path\": \"/baz/0/qux\", \"value\": \"world\"}],\n"
    + "      \"expected\": {\"foo\": [1, 2, 3, 4], \"baz\": [{\"qux\": \"world\"}]} },\n"
    + "\n"
    + "    { \"doc\": [\"foo\"],\n"
    + "      \"patch\": [{\"op\": \"replace\", \"path\": \"/0\", \"value\": \"bar\"}],\n"
    + "      \"expected\": [\"bar\"] },\n"
    + "\n"
    + "    { \"doc\": [\"\"],\n"
    + "      \"patch\": [{\"op\": \"replace\", \"path\": \"/0\", \"value\": 0}],\n"
    + "      \"expected\": [0] },\n"
    + "\n"
    + "    { \"doc\": [\"\"],\n"
    + "      \"patch\": [{\"op\": \"replace\", \"path\": \"/0\", \"value\": true}],\n"
    + "      \"expected\": [true] },\n"
    + "\n"
    + "    { \"doc\": [\"\"],\n"
    + "      \"patch\": [{\"op\": \"replace\", \"path\": \"/0\", \"value\": false}],\n"
    + "      \"expected\": [false] },\n"
    + "\n"
    + "    { \"doc\": [\"\"],\n"
    + "      \"patch\": [{\"op\": \"replace\", \"path\": \"/0\", \"value\": null}],\n"
    + "      \"expected\": [null] },\n"
    + "\n"
    + "    { \"doc\": [\"foo\", \"sil\"],\n"
    + "      \"patch\": [{\"op\": \"replace\", \"path\": \"/1\", \"value\": [\"bar\", \"baz\"]}],\n"
    + "      \"expected\": [\"foo\", [\"bar\", \"baz\"]],\n"
    + "      \"comment\": \"value in array replace not flattened\" },\n"
    + "\n"
    + "    { \"comment\": \"test replace with missing parent key should fail\",\n"
    + "      \"doc\": {\"bar\": \"baz\"},\n"
    + "      \"patch\": [{\"op\": \"replace\", \"path\": \"/foo/bar\", \"value\": false}],\n"
    + "      \"error\": \"replace op should fail with missing parent key\" },\n"
    + "\n"
    + "    { \"comment\": \"spurious patch properties\",\n"
    + "      \"doc\": {\"foo\": 1},\n"
    + "      \"patch\": [{\"op\": \"test\", \"path\": \"/foo\", \"value\": 1, \"spurious\": 1}],\n"
    + "      \"expected\": {\"foo\": 1} },\n"
    + "\n"
    + "    { \"doc\": {\"foo\": null},\n"
    + "      \"patch\": [{\"op\": \"test\", \"path\": \"/foo\", \"value\": null}],\n"
    + "      \"expected\": {\"foo\": null},\n"
    + "      \"comment\": \"null value should be valid obj property\" },\n"
    + "\n"
    + "    { \"doc\": {\"foo\": null},\n"
    + "      \"patch\": [{\"op\": \"replace\", \"path\": \"/foo\", \"value\": \"truthy\"}],\n"
    + "      \"expected\": {\"foo\": \"truthy\"},\n"
    + "      \"comment\": \"null value should be valid obj property to be replaced with something truthy\" },\n"
    + "\n"
    + "    { \"doc\": {\"foo\": null},\n"
    + "      \"patch\": [{\"op\": \"move\", \"from\": \"/foo\", \"path\": \"/bar\"}],\n"
    + "      \"expected\": {\"bar\": null},\n"
    + "      \"comment\": \"null value should be valid obj property to be moved\" },\n"
    + "\n"
    + "    { \"doc\": {\"foo\": null},\n"
    + "      \"patch\": [{\"op\": \"copy\", \"from\": \"/foo\", \"path\": \"/bar\"}],\n"
    + "      \"expected\": {\"foo\": null, \"bar\": null},\n"
    + "      \"comment\": \"null value should be valid obj property to be copied\" },\n"
    + "\n"
    + "    { \"doc\": {\"foo\": null},\n"
    + "      \"patch\": [{\"op\": \"remove\", \"path\": \"/foo\"}],\n"
    + "      \"expected\": {},\n"
    + "      \"comment\": \"null value should be valid obj property to be removed\" },\n"
    + "\n"
    + "    { \"doc\": {\"foo\": \"bar\"},\n"
    + "      \"patch\": [{\"op\": \"replace\", \"path\": \"/foo\", \"value\": null}],\n"
    + "      \"expected\": {\"foo\": null},\n"
    + "      \"comment\": \"null value should still be valid obj property replace other value\" },\n"
    + "\n"
    + "    { \"doc\": {\"foo\": {\"foo\": 1, \"bar\": 2}},\n"
    + "      \"patch\": [{\"op\": \"test\", \"path\": \"/foo\", \"value\": {\"bar\": 2, \"foo\": 1}}],\n"
    + "      \"expected\": {\"foo\": {\"foo\": 1, \"bar\": 2}},\n"
    + "      \"comment\": \"test should pass despite rearrangement\" },\n"
    + "\n"
    + "    { \"doc\": {\"foo\": [{\"foo\": 1, \"bar\": 2}]},\n"
    + "      \"patch\": [{\"op\": \"test\", \"path\": \"/foo\", \"value\": [{\"bar\": 2, \"foo\": 1}]}],\n"
    + "      \"expected\": {\"foo\": [{\"foo\": 1, \"bar\": 2}]},\n"
    + "      \"comment\": \"test should pass despite (nested) rearrangement\" },\n"
    + "\n"
    + "    { \"doc\": {\"foo\": {\"bar\": [1, 2, 5, 4]}},\n"
    + "      \"patch\": [{\"op\": \"test\", \"path\": \"/foo\", \"value\": {\"bar\": [1, 2, 5, 4]}}],\n"
    + "      \"expected\": {\"foo\": {\"bar\": [1, 2, 5, 4]}},\n"
    + "      \"comment\": \"test should pass - no error\" },\n"
    + "\n"
    + "    { \"doc\": {\"foo\": {\"bar\": [1, 2, 5, 4]}},\n"
    + "      \"patch\": [{\"op\": \"test\", \"path\": \"/foo\", \"value\": [1, 2]}],\n"
    + "      \"error\": \"test op should fail\" },\n"
    + "\n"
    + "    { \"comment\": \"Empty-string element\",\n"
    + "      \"doc\": { \"\": 1 },\n"
    + "      \"patch\": [{\"op\": \"test\", \"path\": \"/\", \"value\": 1}],\n"
    + "      \"expected\": { \"\": 1 } },\n"
    + "\n"
    + "    { \"doc\": {\n"
    + "            \"foo\": [\"bar\", \"baz\"],\n"
    + "            \"\": 0,\n"
    + "            \"a/b\": 1,\n"
    + "            \"c%d\": 2,\n"
    + "            \"e^f\": 3,\n"
    + "            \"g|h\": 4,\n"
    + "            \"i\\\\j\": 5,\n"
    + "            \"k\\\"l\": 6,\n"
    + "            \" \": 7,\n"
    + "            \"m~n\": 8\n"
    + "            },\n"
    + "      \"patch\": [{\"op\": \"test\", \"path\": \"/foo\", \"value\": [\"bar\", \"baz\"]},\n"
    + "                {\"op\": \"test\", \"path\": \"/foo/0\", \"value\": \"bar\"},\n"
    + "                {\"op\": \"test\", \"path\": \"/\", \"value\": 0},\n"
    + "                {\"op\": \"test\", \"path\": \"/a~1b\", \"value\": 1},\n"
    + "                {\"op\": \"test\", \"path\": \"/c%d\", \"value\": 2},\n"
    + "                {\"op\": \"test\", \"path\": \"/e^f\", \"value\": 3},\n"
    + "                {\"op\": \"test\", \"path\": \"/g|h\", \"value\": 4},\n"
    + "                {\"op\": \"test\", \"path\":  \"/i\\\\j\", \"value\": 5},\n"
    + "                {\"op\": \"test\", \"path\": \"/k\\\"l\", \"value\": 6},\n"
    + "                {\"op\": \"test\", \"path\": \"/ \", \"value\": 7},\n"
    + "                {\"op\": \"test\", \"path\": \"/m~0n\", \"value\": 8}],\n"
    + "      \"expected\": {\n"
    + "            \"\": 0,\n"
    + "            \" \": 7,\n"
    + "            \"a/b\": 1,\n"
    + "            \"c%d\": 2,\n"
    + "            \"e^f\": 3,\n"
    + "            \"foo\": [\n"
    + "                \"bar\",\n"
    + "                \"baz\"\n"
    + "            ],\n"
    + "            \"g|h\": 4,\n"
    + "            \"i\\\\j\": 5,\n"
    + "            \"k\\\"l\": 6,\n"
    + "            \"m~n\": 8\n"
    + "        }\n"
    + "    },\n"
    + "    { \"comment\": \"Move to same location has no effect\",\n"
    + "      \"doc\": {\"foo\": 1},\n"
    + "      \"patch\": [{\"op\": \"move\", \"from\": \"/foo\", \"path\": \"/foo\"}],\n"
    + "      \"expected\": {\"foo\": 1} },\n"
    + "\n"
    + "    { \"doc\": {\"foo\": 1, \"baz\": [{\"qux\": \"hello\"}]},\n"
    + "      \"patch\": [{\"op\": \"move\", \"from\": \"/foo\", \"path\": \"/bar\"}],\n"
    + "      \"expected\": {\"baz\": [{\"qux\": \"hello\"}], \"bar\": 1} },\n"
    + "\n"
    + "    { \"doc\": {\"baz\": [{\"qux\": \"hello\"}], \"bar\": 1},\n"
    + "      \"patch\": [{\"op\": \"move\", \"from\": \"/baz/0/qux\", \"path\": \"/baz/1\"}],\n"
    + "      \"expected\": {\"baz\": [{}, \"hello\"], \"bar\": 1} },\n"
    + "\n"
    + "    { \"doc\": {\"baz\": [{\"qux\": \"hello\"}], \"bar\": 1},\n"
    + "      \"patch\": [{\"op\": \"copy\", \"from\": \"/baz/0\", \"path\": \"/boo\"}],\n"
    + "      \"expected\": {\"baz\":[{\"qux\":\"hello\"}],\"bar\":1,\"boo\":{\"qux\":\"hello\"}} },\n"
    + "\n"
    + "    { \"comment\": \"Adding to \\\"/-1\\\" adds to the end of the array\",\n"
    + "      \"doc\": [ 1, 2 ],\n"
    + "      \"patch\": [ { \"op\": \"add\", \"path\": \"/-1\", \"value\": { \"foo\": [ \"bar\", \"baz\" ] } } ],\n"
    + "      \"expected\": [ 1, 2, { \"foo\": [ \"bar\", \"baz\" ] } ]},\n"
    + "\n"
    + "    { \"comment\": \"Adding to \\\"/-1\\\" adds to the end of the array, even n levels down\",\n"
    + "      \"doc\": [ 1, 2, [ 3, [ 4, 5 ] ] ],\n"
    + "      \"patch\": [ { \"op\": \"add\", \"path\": \"/2/1/-1\", \"value\": { \"foo\": [ \"bar\", \"baz\" ] } } ],\n"
    + "      \"expected\": [ 1, 2, [ 3, [ 4, 5, { \"foo\": [ \"bar\", \"baz\" ] } ] ] ]},\n"
    + "\n"
    + "    { \"comment\": \"test remove with bad number should fail\",\n"
    + "      \"doc\": {\"foo\": 1, \"baz\": [{\"qux\": \"hello\"}]},\n"
    + "      \"patch\": [{\"op\": \"remove\", \"path\": \"/baz/1e0/qux\"}],\n"
    + "      \"error\": \"remove op shouldn't remove from array with bad number\" },\n"
    + "\n"
    + "    { \"comment\": \"test remove on array\",\n"
    + "      \"doc\": [1, 2, 3, 4],\n"
    + "      \"patch\": [{\"op\": \"remove\", \"path\": \"/0\"}],\n"
    + "      \"expected\": [2, 3, 4] },\n"
    + "\n"
    + "    { \"comment\": \"test repeated removes\",\n"
    + "      \"doc\": [1, 2, 3, 4],\n"
    + "      \"patch\": [{ \"op\": \"remove\", \"path\": \"/1\" },\n"
    + "                { \"op\": \"remove\", \"path\": \"/2\" }],\n"
    + "      \"expected\": [1, 3] },\n"
    + "\n"
    + "    { \"comment\": \"test remove with bad index should fail\",\n"
    + "      \"doc\": [1, 2, 3, 4],\n"
    + "      \"patch\": [{\"op\": \"remove\", \"path\": \"/1e0\"}],\n"
    + "      \"error\": \"remove op shouldn't remove from array with bad number\" },\n"
    + "\n"
    + "    { \"comment\": \"test replace with bad number should fail\",\n"
    + "      \"doc\": [\"\"],\n"
    + "      \"patch\": [{\"op\": \"replace\", \"path\": \"/1e0\", \"value\": false}],\n"
    + "      \"error\": \"replace op shouldn't replace in array with bad number\" },\n"
    + "\n"
    + "    { \"comment\": \"test copy with bad number should fail\",\n"
    + "      \"doc\": {\"baz\": [1,2,3], \"bar\": 1},\n"
    + "      \"patch\": [{\"op\": \"copy\", \"from\": \"/baz/1e0\", \"path\": \"/boo\"}],\n"
    + "      \"error\": \"copy op shouldn't work with bad number\" },\n"
    + "\n"
    + "    { \"comment\": \"test move with bad number should fail\",\n"
    + "      \"doc\": {\"foo\": 1, \"baz\": [1,2,3,4]},\n"
    + "      \"patch\": [{\"op\": \"move\", \"from\": \"/baz/1e0\", \"path\": \"/foo\"}],\n"
    + "      \"error\": \"move op shouldn't work with bad number\" },\n"
    + "\n"
    + "    { \"comment\": \"test add with bad number should fail\",\n"
    + "      \"doc\": [\"foo\", \"sil\"],\n"
    + "      \"patch\": [{\"op\": \"add\", \"path\": \"/1e0\", \"value\": \"bar\"}],\n"
    + "      \"error\": \"add op shouldn't add to array with bad number\" },\n"
    + "\n"
    + "    { \"comment\": \"missing 'path' parameter\",\n"
    + "      \"doc\": {},\n"
    + "      \"patch\": [ { \"op\": \"add\", \"value\": \"bar\" } ],\n"
    + "      \"error\": \"missing 'path' parameter\" },\n"
    + "\n"
    + "    { \"comment\": \"'path' parameter with null value\",\n"
    + "      \"doc\": {},\n"
    + "      \"patch\": [ { \"op\": \"add\", \"path\": null, \"value\": \"bar\" } ],\n"
    + "      \"error\": \"null is not valid value for 'path'\" },\n"
    + "\n"
    + "    { \"comment\": \"invalid JSON Pointer token\",\n"
    + "      \"doc\": {},\n"
    + "      \"patch\": [ { \"op\": \"add\", \"path\": \"foo\", \"value\": \"bar\" } ],\n"
    + "      \"error\": \"JSON Pointer should start with a slash\" },\n"
    + "\n"
    + "    { \"comment\": \"missing 'value' parameter to add\",\n"
    + "      \"doc\": [ 1 ],\n"
    + "      \"patch\": [ { \"op\": \"add\", \"path\": \"/-\" } ],\n"
    + "      \"error\": \"missing 'value' parameter\" },\n"
    + "\n"
    + "    { \"comment\": \"missing 'value' parameter to replace\",\n"
    + "      \"doc\": [ 1 ],\n"
    + "      \"patch\": [ { \"op\": \"replace\", \"path\": \"/0\" } ],\n"
    + "      \"error\": \"missing 'value' parameter\" },\n"
    + "\n"
    + "    { \"comment\": \"missing 'value' parameter to test\",\n"
    + "      \"doc\": [ null ],\n"
    + "      \"patch\": [ { \"op\": \"test\", \"path\": \"/0\" } ],\n"
    + "      \"error\": \"missing 'value' parameter\" },\n"
    + "\n"
    + "    { \"comment\": \"missing value parameter to test - where undef is falsy\",\n"
    + "      \"doc\": [ false ],\n"
    + "      \"patch\": [ { \"op\": \"test\", \"path\": \"/0\" } ],\n"
    + "      \"error\": \"missing 'value' parameter\" },\n"
    + "\n"
    + "    { \"comment\": \"missing from parameter to copy\",\n"
    + "      \"doc\": [ 1 ],\n"
    + "      \"patch\": [ { \"op\": \"copy\", \"path\": \"/-\" } ],\n"
    + "      \"error\": \"missing 'from' parameter\" },\n"
    + "\n"
    + "    { \"comment\": \"missing from location to copy\",\n"
    + "      \"doc\": { \"foo\": 1 },\n"
    + "      \"patch\": [ { \"op\": \"copy\", \"from\": \"/bar\", \"path\": \"/foo\" } ],\n"
    + "      \"error\": \"missing 'from' location\" },\n"
    + "\n"
    + "    { \"comment\": \"missing from parameter to move\",\n"
    + "      \"doc\": { \"foo\": 1 },\n"
    + "      \"patch\": [ { \"op\": \"move\", \"path\": \"\" } ],\n"
    + "      \"error\": \"missing 'from' parameter\" },\n"
    + "\n"
    + "    { \"comment\": \"missing from location to move\",\n"
    + "      \"doc\": { \"foo\": 1 },\n"
    + "      \"patch\": [ { \"op\": \"move\", \"from\": \"/bar\", \"path\": \"/foo\" } ],\n"
    + "      \"error\": \"missing 'from' location\" },\n"
    + "\n"
    + "    { \"comment\": \"unrecognized op should fail\",\n"
    + "      \"doc\": {\"foo\": 1},\n"
    + "      \"patch\": [{\"op\": \"spam\", \"path\": \"/foo\", \"value\": 1}],\n"
    + "      \"error\": \"Unrecognized op 'spam'\" },\n"
    + "\n"
    + "    { \"comment\": \"test with bad array number that has leading zeros\",\n"
    + "      \"doc\": [\"foo\", \"bar\"],\n"
    + "      \"patch\": [{\"op\": \"test\", \"path\": \"/00\", \"value\": \"foo\"}],\n"
    + "      \"error\": \"test op should reject the array value, it has leading zeros\" },\n"
    + "\n"
    + "    { \"comment\": \"test with bad array number that has leading zeros\",\n"
    + "      \"doc\": [\"foo\", \"bar\"],\n"
    + "      \"patch\": [{\"op\": \"test\", \"path\": \"/01\", \"value\": \"bar\"}],\n"
    + "      \"error\": \"test op should reject the array value, it has leading zeros\" },\n"
    + "\n"
    + "    { \"comment\": \"Removing nonexistent field\",\n"
    + "      \"doc\": {\"foo\" : \"bar\"},\n"
    + "      \"patch\": [{\"op\": \"remove\", \"path\": \"/baz\"}],\n"
    + "      \"error\": \"removing a nonexistent field should fail\" },\n"
    + "\n"
    + "    { \"comment\": \"Removing nonexistent index\",\n"
    + "      \"doc\": [\"foo\", \"bar\"],\n"
    + "      \"patch\": [{\"op\": \"remove\", \"path\": \"/2\"}],\n"
    + "      \"error\": \"removing a nonexistent index should fail\" },\n"
    + "\n"
    + "    { \"comment\": \"Patch with different capitalisation than doc\",\n"
    + "       \"doc\": {\"foo\":\"bar\"},\n"
    + "       \"patch\": [{\"op\": \"add\", \"path\": \"/FOO\", \"value\": \"BAR\"}],\n"
    + "       \"expected\": {\"foo\": \"bar\", \"FOO\": \"BAR\"}\n"
    + "    }\n"
    + "\n"
    + "]\n";

    @Test
    public void test_json_patch() throws MalformedJson, PatchMalformed, PatchOpError
    {
        final JsArray patches = JsArray.parse(tests)
                                       .orElseThrow();

        for (JsElem test : patches)
        {

            final JsObj obj = test.asJsObj();


            final JsElem expected = obj.get(JsPath.fromKey("expected"));

            final JsElem error = obj.get(JsPath.fromKey("expected"));


            if (!error.isNothing())
            {
                Assertions.assertEquals(expected,
                                        obj.get(JsPath.fromKey("doc"))
                                           .asJson()
                                           .patch(obj.get(JsPath.fromKey("patch"))
                                                     .asJsArray())
                                           .orElseThrow()
                                       );
            } else
            {

                Assertions.assertThrows(Exception.class,
                                        () -> obj.get(JsPath.fromKey("doc"))
                                                 .asJson()
                                                 .patch(obj.get(JsPath.fromKey("patch"))
                                                           .asJsArray())
                                                 .orElseThrow()
                                       );
            }

        }

    }
}
