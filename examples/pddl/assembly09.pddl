
(DEFINE (PROBLEM ASSEM-7)
   (:DOMAIN ASSEMBLY)
   (:OBJECTS WIDGET HOOZAWHATSIE UNIT FASTENER SOCKET PLUG WIRE
             CONTRAPTION FOOBAR KLUDGE THINGUMBOB HACK WHATSIS CONNECTOR DEVICE
             COIL BRACKET - ASSEMBLY
             HAMMOCK CLAMP - RESOURCE)
   (:INIT (AVAILABLE UNIT)
          (AVAILABLE FASTENER)
          (AVAILABLE PLUG)
          (AVAILABLE WIRE)
          (AVAILABLE FOOBAR)
          (AVAILABLE KLUDGE)
          (AVAILABLE HACK)
          (AVAILABLE WHATSIS)
          (AVAILABLE CONNECTOR)
          (AVAILABLE DEVICE)
          (AVAILABLE COIL)
          (AVAILABLE BRACKET)
          (REQUIRES HOOZAWHATSIE CLAMP)
          (REQUIRES SOCKET CLAMP)
          (REQUIRES CONTRAPTION HAMMOCK)
          (REQUIRES THINGUMBOB CLAMP)
          (PART-OF HOOZAWHATSIE WIDGET)
          (PART-OF SOCKET WIDGET)
          (PART-OF CONTRAPTION WIDGET)
          (PART-OF THINGUMBOB WIDGET)
          (PART-OF UNIT HOOZAWHATSIE)
          (PART-OF FASTENER HOOZAWHATSIE)
          (PART-OF PLUG SOCKET)
          (PART-OF WIRE SOCKET)
          (PART-OF FOOBAR CONTRAPTION)
          (PART-OF KLUDGE CONTRAPTION)
          (PART-OF HACK THINGUMBOB)
          (PART-OF WHATSIS THINGUMBOB)
          (PART-OF CONNECTOR THINGUMBOB)
          (PART-OF DEVICE THINGUMBOB)
          (PART-OF COIL THINGUMBOB)
          (PART-OF BRACKET THINGUMBOB)
          (ASSEMBLE-ORDER CONTRAPTION THINGUMBOB WIDGET)
          (ASSEMBLE-ORDER FASTENER UNIT HOOZAWHATSIE)
          (ASSEMBLE-ORDER WIRE PLUG SOCKET)
          (ASSEMBLE-ORDER FOOBAR KLUDGE CONTRAPTION)
          (ASSEMBLE-ORDER WHATSIS CONNECTOR THINGUMBOB)
          (ASSEMBLE-ORDER CONNECTOR BRACKET THINGUMBOB)
          (ASSEMBLE-ORDER CONNECTOR HACK THINGUMBOB)
          (ASSEMBLE-ORDER DEVICE COIL THINGUMBOB)
          (ASSEMBLE-ORDER COIL WHATSIS THINGUMBOB))
   (:GOAL (COMPLETE WIDGET)))
