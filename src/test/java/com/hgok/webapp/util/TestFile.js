function foo(alma){
    console.log(alma);
}

function bar(){
    let asd = "foo";
    foo(asd);
}

function main(){
    let asd = "foo";
    bar();
    foo(asd)
}
