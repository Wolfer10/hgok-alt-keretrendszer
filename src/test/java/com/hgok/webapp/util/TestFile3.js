function foo(alma){
    console.log(alma);
}

function foo2(alma){
    console.log(alma);
}

function foo3(alma){
    console.log(alma);
    bar2();
}


function bar(){
    let asd = "foo";
    foo(asd);
}

function bar2(){
    let asd = "foo";
    foo(asd);
}


function main(){
    let asd = "foo";
    bar();
    foo(asd)
}
